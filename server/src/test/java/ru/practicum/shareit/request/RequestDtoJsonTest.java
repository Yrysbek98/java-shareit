package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import ru.practicum.shareit.request.dto.RequestDto;


import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class RequestDtoJsonTest {
    @Autowired
    private JacksonTester<RequestDto> json;


    @Test
    void testDeserialize() throws Exception {
        String content = "{\"description\":\"Нужна дрель\"," +
                "\"created\":\"2024-12-10T10:00:00\"}";

        RequestDto result = json.parse(content).getObject();

        assertThat(result.getDescription()).isEqualTo("Нужна дрель");

    }


}
