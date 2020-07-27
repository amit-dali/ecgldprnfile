package nl.ebay.creditlimittracker.util;

import info.solidsoft.mockito.java8.api.WithBDDMockito;
import nl.ebay.creditlimittracker.exception.handlers.JsonConversionException;
import nl.ebay.creditlimittracker.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JsonConverterUtilTest implements WithBDDMockito {

    @Test
    @DisplayName("Should convert Name to Json")
    void convertToJson() {
        // given
        User user = User.builder().name("R.J.").address("Amsterdam").build();
        // when
        String json = JsonConverterUtil.convertToJson(user);

        // then
        assertEquals("{\"name\":\"R.J.\",\"address\":\"Amsterdam\"}", json);
    }

    @Test
    @DisplayName("Should throw JsonConversionException while converting mock object")
    void exceptionConvToJson() {
        User notSerilizableObj = mock(User.class);

        assertThrows(JsonConversionException.class, () -> JsonConverterUtil.convertToJson(notSerilizableObj));
    }

    @Test
    @DisplayName("Should instantiate json to given object")
    void convertFromJson() {
        // given
        String json = "{\"name\":\"R.J.\",\"address\":\"Amsterdam\"}";

        // when
        User user = JsonConverterUtil.convertFromJson(json, User.class);

        // then
        assertEquals("R.J.", user.getName());
        assertEquals("Amsterdam", user.getAddress());
    }

    @Test
    @DisplayName("Should throw JsonConversionException on converting invalid json")
    void exceptionTestFromJson() {
        String input = "invalid json";

        assertThrows(JsonConversionException.class, () -> JsonConverterUtil.convertFromJson(input, User.class));
    }

}
