package backend.controller;

import backend.api.request.AddInstrumentToWatchlistRequest;
import backend.api.response.InstrumentResponse;
import backend.persistence.entity.Instrument;
import backend.service.MarketDirectoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/instruments")
@RequiredArgsConstructor
public class InstrumentController {

    private final MarketDirectoryService marketDirectoryService;

    @GetMapping
    public ResponseEntity<List<InstrumentResponse>> getEnabledMoexInstruments() {
        List<InstrumentResponse> instruments = marketDirectoryService.getEnabledMoexInstruments()
                .stream()
                .map(this::toResponse)
                .sorted(Comparator.comparing(InstrumentResponse::id))
                .toList();
        return ResponseEntity.ok().body(instruments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstrumentResponse> getInstrumentById(@PathVariable Long id) {
        return marketDirectoryService.getInstrumentById(id)
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/watchlist")
    public ResponseEntity<InstrumentResponse> addToWatchlist(
            @RequestBody AddInstrumentToWatchlistRequest request) {
        return marketDirectoryService.addToWatchlist(request.instrumentId())
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private InstrumentResponse toResponse(Instrument instrument) {
        return new InstrumentResponse(
                instrument.getId(), instrument.getTicker(), instrument.getName(),
                instrument.getMarket(), instrument.getBoard(), instrument.getCurrency(), instrument.getEnabled()
        );
    }

}
