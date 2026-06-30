package backend.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UtilsTest {

    @Test
    void shouldReturnNullWhenValueIsNull() {
        Assertions.assertNull(Utils.parseLocalTime(null));
        Assertions.assertNull(Utils.parseLocalDateTime(null));
    }


}