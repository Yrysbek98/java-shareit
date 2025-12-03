package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserResponseDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserDtoJsonTest {

    @Autowired
    private JacksonTester<UserResponseDto> json;

    @Test
    void testSerialize() throws Exception {
        UserResponseDto userDto = new UserResponseDto(1L, "John Doe", "john@example.com");

        JsonContent<UserResponseDto> result = json.write(userDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("John Doe");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("john@example.com");
    }

    @Test
    void testDeserialize() throws Exception {
        String content = "{\"id\":1,\"name\":\"John Doe\",\"email\":\"john@example.com\"}";

        UserResponseDto result = json.parse(content).getObject();

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("John Doe");
        assertThat(result.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void testSerializeWithNullId() throws Exception {
        UserResponseDto userDto = new UserResponseDto(null, "John Doe", "john@example.com");

        JsonContent<UserResponseDto> result = json.write(userDto);

        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("John Doe");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("john@example.com");
    }

    @Test
    void testEmailValidation() throws Exception {
        String content = "{\"id\":1,\"name\":\"John Doe\",\"email\":\"invalid-email\"}";

        UserResponseDto result = json.parse(content).getObject();

        assertThat(result.getEmail()).isEqualTo("invalid-email");
    }
}
