package backend.api.response;

public record InstrumentResponse(
        Long id,
        String ticker,
        String name,
        String market,
        String board,
        String currency,
        Boolean enabled
) {
}
