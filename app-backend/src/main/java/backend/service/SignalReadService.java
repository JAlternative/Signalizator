package backend.service;

import backend.api.response.SignalResponse;
import backend.domain.SignalStatus;
import backend.persistence.entity.Signal;
import backend.persistence.repository.SignalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SignalReadService {

    private final SignalRepository signalRepository;

    @Transactional(readOnly = true)
    public List<SignalResponse> getLatestNewSignals() {
        return signalRepository
                .findTop100ByStatusOrderByCreatedAtDesc(SignalStatus.NEW.name())
                .stream().map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<SignalResponse> getSignalById(Long id) {
        return signalRepository
                .findById(id)
                .map(this::toResponse);
    }

    private SignalResponse toResponse(Signal signal) {
        Long pricePointId = signal.getPricePoint() == null
                ? null
                : signal.getPricePoint().getId();

        return new SignalResponse(
                signal.getId(),
                signal.getInstrument().getId(),
                signal.getInstrument().getTicker(),
                signal.getRule().getId(),
                signal.getRule().getCode(),
                pricePointId,
                signal.getType(),
                signal.getSeverity(),
                signal.getTitle(),
                signal.getExplanation(),
                signal.getFactsJson(),
                signal.getStatus(),
                signal.getDedupKey(),
                signal.getCreatedAt()
        );
    }
}
