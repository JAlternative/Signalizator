package backend.controller;

import backend.api.response.SignalResponse;
import backend.persistence.entity.Signal;
import backend.service.PriceChangeSignalService;
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
}
