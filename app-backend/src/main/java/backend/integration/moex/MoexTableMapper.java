package backend.integration.moex;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class MoexTableMapper {


    private final ObjectMapper objectMapper;

    public <T> Optional<T> mapFirstRow(String rawJson, String tableName, Class<T> type) {
        try {
            JsonNode root = objectMapper.readTree(rawJson);

            JsonNode table = root.get(tableName);
            if (table == null || table.isNull()) {
                return Optional.empty();
            }

            JsonNode columns = table.get("columns");
            JsonNode data = table.get("data");

            if (columns == null || data == null || !columns.isArray() || !data.isArray() || data.isEmpty()) {
                return Optional.empty();
            }

            JsonNode row = data.get(0);

            Map<String, Object> rowAsMap = new LinkedHashMap<>();

            for (int i = 0; i < columns.size(); i++) {
                String columnName = columns.get(i).asText();
                JsonNode value = i < row.size() ? row.get(i) : null;

                Object cellValue = value == null || value.isNull()
                        ? null
                        : objectMapper.treeToValue(value, Object.class);

                rowAsMap.put(columnName, cellValue);
            }

            T result = objectMapper.convertValue(rowAsMap, type);
            return Optional.of(result);
        } catch (Exception e) {
            log.error("Не удалось преобразовать MOEX JSON table={} в {}", tableName, type.getSimpleName(), e);
            return Optional.empty();
        }
    }
}