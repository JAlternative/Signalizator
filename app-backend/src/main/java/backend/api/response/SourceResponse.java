package backend.api.response;

public record SourceResponse(
        Long id,
        String code,
        String name,
        String baseUrl,
        Boolean enabled
) {
}
