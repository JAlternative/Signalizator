package backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MoexWatchlistScheduler {

    private final MoexWatchlistCollectorService moexWatchlistCollectorService;


//    @Scheduled(fixedDelayString = "${app.moex.collector.fixed-delay-ms}")
    public void collectWatchlistBySchedule() {
        List<Long> pricePoints = moexWatchlistCollectorService.collectEnabledMoexWatchlistOnce();
        log.info("MOEX watchlist collector finished. Saved pricePoint ids: {}", pricePoints);
    }
}
