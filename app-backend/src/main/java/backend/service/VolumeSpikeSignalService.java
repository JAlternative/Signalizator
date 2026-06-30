package backend.service;

import backend.domain.SignalSeverity;
import backend.domain.SignalStatus;
import backend.domain.SignalType;
import backend.persistence.entity.PricePoint;
import backend.persistence.entity.Rule;
import backend.persistence.entity.Signal;
import backend.persistence.repository.PricePointRepository;
import backend.persistence.repository.RuleRepository;
import backend.persistence.repository.SignalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VolumeSpikeSignalService {

    private static final long THRESHOLD_MULTIPLIER = 3;

    private final PricePointRepository pricePointRepository;
    private final RuleRepository ruleRepository;
    private final SignalRepository signalRepository;

    @Transactional
    public void generateForInstrument(Long instrumentId) {
        List<PricePoint> pricePointList =
                pricePointRepository.findTop100ByInstrumentIdOrderBySystemTimeDesc(instrumentId);

        if (pricePointList.size() < 3) {
            return;
        }

        PricePoint current = pricePointList.get(0);
        PricePoint previous = pricePointList.get(1);

        Long currentVolumeToday = current.getVolumeToday();
        Long previousVolumeToday = previous.getVolumeToday();

        if (currentVolumeToday == null || previousVolumeToday == null) {
            return;
        }

        long currentDelta = currentVolumeToday - previousVolumeToday;

        if (currentDelta <= 0) {
            return;
        }

        long sumPreviousDeltas = 0;
        int countPreviousDeltas = 0;

        for (int i = 1; i < pricePointList.size() - 1; i++) {
            PricePoint newer = pricePointList.get(i);
            PricePoint older = pricePointList.get(i + 1);

            Long newerVolumeToday = newer.getVolumeToday();
            Long olderVolumeToday = older.getVolumeToday();

            if (newerVolumeToday == null || olderVolumeToday == null) {
                continue;
            }

            long delta = newerVolumeToday - olderVolumeToday;

            if (delta <= 0) {
                continue;
            }

            sumPreviousDeltas += delta;
            countPreviousDeltas++;
        }

        if (countPreviousDeltas == 0) {
            return;
        }

        long averageDelta = sumPreviousDeltas / countPreviousDeltas;

        if (averageDelta <= 0) {
            return;
        }

        boolean isVolumeSpike = currentDelta >= averageDelta * THRESHOLD_MULTIPLIER;

        if (!isVolumeSpike) {
            return;
        }

        Optional<Rule> ruleOptional = ruleRepository.findByCode(SignalType.VOLUME_SPIKE.name())
                .filter(rule -> Boolean.TRUE.equals(rule.getEnabled()));

        if (ruleOptional.isEmpty()) {
            return;
        }

        Rule rule = ruleOptional.get();

        String dedupKey = SignalType.VOLUME_SPIKE.name()
                + ":" + instrumentId
                + ":" + current.getId();

        if (signalRepository.existsByDedupKey(dedupKey)) {
            return;
        }

        Map<String, Object> factsJson = new LinkedHashMap<>();
        factsJson.put("currentPricePointId", current.getId());
        factsJson.put("previousPricePointId", previous.getId());
        factsJson.put("currentVolumeToday", currentVolumeToday);
        factsJson.put("previousVolumeToday", previousVolumeToday);
        factsJson.put("currentDelta", currentDelta);
        factsJson.put("averageDelta", averageDelta);
        factsJson.put("thresholdMultiplier", THRESHOLD_MULTIPLIER);

        Signal signal = new Signal();
        signal.setInstrument(current.getInstrument());
        signal.setRule(rule);
        signal.setPricePoint(current);
        signal.setType(SignalType.VOLUME_SPIKE.name());
        signal.setSeverity(SignalSeverity.MEDIUM.name());
        signal.setTitle("Объём торгов " + current.getSecId() + " резко вырос");
        signal.setExplanation(
                "Текущий прирост объёма торгов заметно выше среднего предыдущего прироста. " +
                        "Это может означать повышенную активность участников рынка по инструменту."
        );
        signal.setFactsJson(factsJson);
        signal.setStatus(SignalStatus.NEW.name());
        signal.setDedupKey(dedupKey);

        signalRepository.save(signal);
    }
}