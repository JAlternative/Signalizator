package backend.utils;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.Optional;

@NoArgsConstructor
@Slf4j
public final class Utils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static Optional<JsonNode> readTree(String raw) {
        try {
            JsonNode node = MAPPER.readTree(raw);
            return Optional.of(node);
        } catch (Exception ignore) {
            log.error("readTree, не удалось преобразовать строку в объект: " + ignore.getMessage());
            return Optional.empty();
        }
    }

    public static <T> Optional<T> readTree(String rawJson, Class<T> type) {
        try {
            T value = MAPPER.readValue(rawJson, type);
            return Optional.of(value);
        } catch (Exception ignore) {
            log.error("readTree, не удалось преобразовать строку в объект: " + ignore.getMessage());
            return Optional.empty();
        }
    }


}
