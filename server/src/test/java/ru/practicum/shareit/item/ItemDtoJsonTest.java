package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemDtoJsonTest {

    @Autowired
    private JacksonTester<ItemResponseDto> json;

    @Test
    void testSerialize() throws Exception {
        ItemResponseDto itemDto = ItemResponseDto.builder()
                .id(1L)
                .name("Дрель")
                .description("Простая дрель")
                .available(true)
                .requestId(5L)
                .build();

        JsonContent<ItemResponseDto> result = json.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Дрель");
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("Простая дрель");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isTrue();
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(5);
    }

    @Test
    void testDeserialize() throws Exception {
        String content = "{\"id\":1,\"name\":\"Дрель\"," +
                "\"description\":\"Простая дрель\",\"available\":true," +
                "\"requestId\":5}";

        ItemResponseDto result = json.parse(content).getObject();

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Дрель");
        assertThat(result.getDescription()).isEqualTo("Простая дрель");
        assertThat(result.getAvailable()).isTrue();
        assertThat(result.getRequestId()).isEqualTo(5L);
    }
}