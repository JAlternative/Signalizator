package backend.controller;

import backend.api.request.AddInstrumentToWatchlistRequest;
import backend.api.response.InstrumentResponse;
import backend.api.response.PricePointResponse;
import backend.persistence.entity.Instrument;
import backend.persistence.entity.PricePoint;
import backend.service.MarketDirectoryService;
import backend.service.MoexMarketDataService;
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
    private final MoexMarketDataService moexMarketDataService;

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

    @GetMapping("/{id}/price-points")
    public ResponseEntity<List<PricePointResponse>> getPricePointHistory(@PathVariable Long id) {
        List<PricePointResponse> response = moexMarketDataService.getPricePointHistory(id)
                .stream()
                .map(this::toPricePointResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    private InstrumentResponse toResponse(Instrument instrument) {
        return new InstrumentResponse(
                instrument.getId(), instrument.getTicker(), instrument.getName(),
                instrument.getMarket(), instrument.getBoard(), instrument.getCurrency(), instrument.getEnabled()
        );
    }

    private PricePointResponse toPricePointResponse(PricePoint pricePoint) {
        return new PricePointResponse(
                pricePoint.getId(),
                pricePoint.getInstrument().getId(),
                pricePoint.getSecId(),
                pricePoint.getBoardId(),
                pricePoint.getBid(),
                pricePoint.getOffer(),
                pricePoint.getSpread(),
                pricePoint.getOpenPrice(),
                pricePoint.getLowPrice(),
                pricePoint.getHighPrice(),
                pricePoint.getLastPrice(),
                pricePoint.getWaprice(),
                pricePoint.getChange(),
                pricePoint.getNumTrades(),
                pricePoint.getVolumeToday(),
                pricePoint.getValueToday(),
                pricePoint.getTradingStatus(),
                pricePoint.getUpdateTime(),
                pricePoint.getSystemTime(),
                pricePoint.getCreatedAt()
        );
    }

}
