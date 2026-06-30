package backend.service;

import backend.persistence.entity.PricePoint;
import backend.persistence.entity.Rule;
import backend.persistence.entity.Signal;
import backend.persistence.repository.PricePointRepository;
import backend.persistence.repository.RuleRepository;
import backend.persistence.repository.SignalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static backend.domain.SignalSeverity.MEDIUM;
import static backend.domain.SignalStatus.NEW;
import static backend.domain.SignalType.PRICE_CHANGE;

@Service
@RequiredArgsConstructor
public class PriceChangeSignalService {

    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);
    private static final BigDecimal PRICE_CHANGE_THRESHOLD_PERCENT = BigDecimal.valueOf(0.5);

    private static final String PRICE_CHANGE_EXPLANATION =
            "Последняя цена изменилась относительно предыдущего snapshot более чем на 0.5%";

    private final PricePointRepository pricePointRepository;
    private final RuleRepository ruleRepository;
    private final SignalRepository signalRepository;

    /**
     * берём последние 2 PricePoint
     * сравниваем lastPrice
     * если изменение сильное
     * создаём Signal
     * */
    @Transactional
    public Optional<Signal> generateForInstrument(Long instrumentId) {
        List<PricePoint> points =
                pricePointRepository.findTop2ByInstrumentIdOrderBySystemTimeDescIdDesc(instrumentId);

        if (points.size() < 2) {
            return Optional.empty();
        }

        PricePoint current = points.get(0);
        PricePoint previous = points.get(1);

        if (current.getLastPrice() == null || previous.getLastPrice() == null) {
            return Optional.empty();
        }

        BigDecimal currentPrice = current.getLastPrice();
        BigDecimal previousPrice = previous.getLastPrice();

        if (previousPrice.compareTo(BigDecimal.ZERO) == 0) {
            return Optional.empty();
        }

        BigDecimal changePercent = currentPrice
                .subtract(previousPrice)
                .divide(previousPrice, 6, RoundingMode.HALF_UP)
                .multiply(ONE_HUNDRED);

        BigDecimal absChangePercent = changePercent.abs();

        if (absChangePercent.compareTo(PRICE_CHANGE_THRESHOLD_PERCENT) < 0) {
            return Optional.empty();
        }

        Optional<Rule> ruleOptional = ruleRepository.findByCode(PRICE_CHANGE.name())
                .filter(rule -> Boolean.TRUE.equals(rule.getEnabled()));

        if (ruleOptional.isEmpty()) {
            return Optional.empty();
        }

        Rule rule = ruleOptional.get();

        String dedupKey = "%s:%d:%d".formatted(
                PRICE_CHANGE.name(),
                instrumentId,
                current.getId()
        );

        if (signalRepository.existsByDedupKey(dedupKey)) {
            return Optional.empty();
        }

        /*
         * PRICE_CHANGE = тип сигнала
         * MEDIUM = важность
         * NEW = новый сигнал
         * dedupKey = защита от дублей
         * */

        Signal signal = new Signal();

        signal.setInstrument(current.getInstrument());
        signal.setRule(rule);
        signal.setPricePoint(current);
        signal.setType(PRICE_CHANGE.name());
        signal.setSeverity(MEDIUM.name());
        signal.setTitle("Цена " + current.getSecId() + " заметно изменилась");
        signal.setExplanation(PRICE_CHANGE_EXPLANATION);
        signal.setStatus(NEW.name());
        signal.setDedupKey(dedupKey);

        signal.setFactsJson(Map.of(
                "previousPricePointId", previous.getId(),
                "currentPricePointId", current.getId(),
                "previousPrice", previousPrice,
                "currentPrice", currentPrice,
                "changePercent", changePercent,
                "absChangePercent", absChangePercent,
                "thresholdPercent", PRICE_CHANGE_THRESHOLD_PERCENT
        ));

        Signal saved = signalRepository.save(signal);
        return Optional.of(saved);
    }
}