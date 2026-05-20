package backend.controller;

import backend.api.response.SourceResponse;
import backend.service.MarketDirectoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/sources")
@RequiredArgsConstructor
public class SourceController {

    private final MarketDirectoryService marketDirectoryService;

    @GetMapping
    public ResponseEntity<List<SourceResponse>> getEnabledSources() {
      List<SourceResponse> sourceResponseList = marketDirectoryService.getEnabledSources()
                .stream()
                .map(e -> new SourceResponse(
                        e.getId(), e.getCode(), e.getName(),
                        e.getBaseUrl(), e.getEnabled()
                )).sorted(Comparator.comparing(SourceResponse::id))
                .toList();
      return ResponseEntity.ok().body(sourceResponseList);
    }
}
