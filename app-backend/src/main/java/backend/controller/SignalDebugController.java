package backend.controller;

import backend.persistence.entity.Signal;
import backend.service.PriceChangeSignalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/signals")
public class SignalDebugController {

    private final PriceChangeSignalService priceChangeSignalService;

    @PostMapping("/price-change/instruments/{id}/generate")
    public ResponseEntity<Long> generatePriceChangeSignal(@PathVariable Long id) {
        return priceChangeSignalService.generateForInstrument(id)
                .map(Signal::getId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
}
