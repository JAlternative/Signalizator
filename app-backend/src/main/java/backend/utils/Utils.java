package backend.utils;

import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@NoArgsConstructor
public final class Utils {

    public static LocalTime parseLocalTime(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return LocalTime.parse(value);
    }

    public static LocalDateTime parseLocalDateTime(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return LocalDateTime.parse(value.replace(" ", "T"));
    }
}
