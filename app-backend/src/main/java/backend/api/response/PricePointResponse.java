package backend.api.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;

public record PricePointResponse(
        Long id,
        Long instrumentId,
        String secId,
        String boardId,
        BigDecimal bid,
        BigDecimal offer,
        BigDecimal spread,
        BigDecimal openPrice,
        BigDecimal lowPrice,
        BigDecimal highPrice,
        BigDecimal lastPrice,
        BigDecimal waprice,
        BigDecimal change,
        Integer numTrades,
        Long volumeToday,
        Long valueToday,
        String tradingStatus,
        LocalTime updateTime,
        LocalDateTime systemTime,
        OffsetDateTime createdAt
) {
}
