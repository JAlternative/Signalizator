package backend.controller;

import backend.service.MoexMarketDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/moex")
@RequiredArgsConstructor
public class MoexDebugController {

    private final MoexMarketDataService moexMarketDataService;

    @GetMapping("/instruments/{id}/raw")
    public ResponseEntity<String> getRawSecurityDataByInstrumentId(
            @PathVariable Long id
    ) {
        Optional<String> rawJson = moexMarketDataService.getRawSecurityDataByInstrumentId(id);
        return rawJson.map(string -> ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(string)).orElseGet(() -> ResponseEntity.notFound().build());

    }

}
