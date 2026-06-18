package backend.service;

import backend.domain.SignalStatus;
import backend.persistence.entity.Signal;
import backend.persistence.repository.SignalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SignalCommandService {

    private final SignalRepository signalRepository;

    @Transactional
    public Optional<Signal> ack(Long id) {
        Optional<Signal> signalOptional = signalRepository.findById(id);

        if (signalOptional.isEmpty()) {
            return Optional.empty();
        }

        Signal signal = signalOptional.get();
        signal.setStatus(SignalStatus.ACK.name());

        return Optional.of(signal);
    }

    @Transactional
    public Optional<Signal> mute(Long id) {
        Optional<Signal> signalOptional = signalRepository.findById(id);

        if (signalOptional.isEmpty()) {
            return Optional.empty();
        }

        Signal signal = signalOptional.get();
        signal.setStatus(SignalStatus.MUTED.name());

        return Optional.of(signal);
    }

}