package backend.controller;

import backend.integration.moex.dto.MoexMarketDataSnapshot;
import backend.persistence.entity.PricePoint;
import backend.service.MoexMarketDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/moex")
@RequiredArgsConstructor
public class MoexDebugController {

    private final MoexMarketDataService moexMarketDataService;

    @GetMapping("/instruments/{id}/raw")
    public ResponseEntity<String> getRawSecurityDataByInstrumentId(@PathVariable Long id) {
        Optional<String> rawJson = moexMarketDataService.getRawSecurityDataByInstrumentId(id);

        return rawJson
                .map(json -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(json))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/instruments/{id}/snapshot")
    public ResponseEntity<MoexMarketDataSnapshot> getMarketDataSnapshotByInstrumentId(@PathVariable Long id) {
        return moexMarketDataService.getMarketDataSnapshotByInstrumentId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/instruments/{id}/collect")
    public ResponseEntity<Long> collectAndSaveSnapshot(@PathVariable Long id) {
        return moexMarketDataService.collectAndSaveSnapshot(id)
                .map(PricePoint::getId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}