package backend.api.response;

import java.time.OffsetDateTime;
import java.util.Map;

public record SignalResponse(
        Long id,
        Long instrumentId,
        String ticker,
        Long ruleId,
        String ruleCode,
        Long pricePointId,
        String type,
        String severity,
        String title,
        String explanation,
        Map<String, Object> factsJson,
        String status,
        String dedupKey,
        OffsetDateTime createdAt
) {
}