package backend.controller;

import backend.api.response.SignalResponse;
import backend.persistence.entity.Signal;
import backend.service.PriceChangeSignalService;
import backend.service.SignalCommandService;
import backend.service.SignalReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/signals")
public class SignalDebugController {

    private final PriceChangeSignalService priceChangeSignalService;
    private final SignalReadService signalReadService;
    private final SignalCommandService signalCommandService;

    @PostMapping("/price-change/instruments/{id}/generate")
    public ResponseEntity<Long> generatePriceChangeSignal(@PathVariable Long id) {
        return priceChangeSignalService.generateForInstrument(id)
                .map(Signal::getId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping
    public ResponseEntity<List<SignalResponse>> getLatestSignals() {
        return ResponseEntity.ok(signalReadService.getLatestNewSignals());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SignalResponse> getSignalById(@PathVariable Long id) {
        return signalReadService
                .getSignalById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/ack")
    public ResponseEntity<Void> ackSignal(@PathVariable Long id) {
        return signalCommandService.ack(id)
                .map(signal -> ResponseEntity.noContent().<Void>build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/mute")
    public ResponseEntity<Void> muteSignal(@PathVariable Long id) {
        return signalCommandService.mute(id)
                .map(signal -> ResponseEntity.noContent().<Void>build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
