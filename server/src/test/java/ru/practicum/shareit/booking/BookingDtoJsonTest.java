package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
class BookingDtoJsonTest {

    @Autowired
    private JacksonTester<BookingResponseDto> json;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Test
    void testSerialize() throws Exception {

        LocalDateTime start = LocalDateTime.of(2024, 12, 10, 10, 0);
        LocalDateTime end = LocalDateTime.of(2024, 12, 15, 10, 0);

        UserResponseDto booker = UserResponseDto.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .build();

        ItemResponseDto item = ItemResponseDto.builder()
                .id(1L)
                .name("Дрель")
                .description("Простая дрель")
                .available(true)
                .build();

        BookingResponseDto bookingDto = BookingResponseDto.builder()
                .id(1L)
                .start(start)
                .end(end)
                .item(item)
                .booker(booker)
                .status(BookingStatus.WAITING)
                .build();


        JsonContent<BookingResponseDto> result = json.write(bookingDto);


        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start")
                .isEqualTo(start.format(FORMATTER));
        assertThat(result).extractingJsonPathStringValue("$.end")
                .isEqualTo(end.format(FORMATTER));
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo("Дрель");
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.booker.name").isEqualTo("John Doe");
        assertThat(result).extractingJsonPathStringValue("$.status")
                .isEqualTo("WAITING");
    }

    @Test
    void testDeserialize() throws Exception {

        String content = "{\"id\":1,\"start\":\"2024-12-10T10:00:00\"," +
                "\"end\":\"2024-12-15T10:00:00\"," +
                "\"item\":{\"id\":1,\"name\":\"Дрель\",\"description\":\"Простая дрель\",\"available\":true}," +
                "\"booker\":{\"id\":1,\"name\":\"John Doe\",\"email\":\"john@example.com\"}," +
                "\"status\":\"WAITING\"}";

        BookingResponseDto result = json.parse(content).getObject();


        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getStart()).isEqualTo(LocalDateTime.of(2024, 12, 10, 10, 0));
        assertThat(result.getEnd()).isEqualTo(LocalDateTime.of(2024, 12, 15, 10, 0));
        assertThat(result.getItem().getId()).isEqualTo(1L);
        assertThat(result.getItem().getName()).isEqualTo("Дрель");
        assertThat(result.getBooker().getId()).isEqualTo(1L);
        assertThat(result.getBooker().getName()).isEqualTo("John Doe");
        assertThat(result.getStatus()).isEqualTo(BookingStatus.WAITING);
    }

    @Test
    void testDateTimeFormatting() throws Exception {

        LocalDateTime specificDateTime = LocalDateTime.of(2024, 1, 15, 14, 30, 45);

        UserResponseDto booker = UserResponseDto.builder()
                .id(1L)
                .name("Jane Doe")
                .email("jane@example.com")
                .build();

        ItemResponseDto item = ItemResponseDto.builder()
                .id(2L)
                .name("Отвертка")
                .description("Крестовая отвертка")
                .available(true)
                .build();

        BookingResponseDto bookingDto = BookingResponseDto.builder()
                .id(1L)
                .start(specificDateTime)
                .end(specificDateTime.plusDays(3))
                .item(item)
                .booker(booker)
                .status(BookingStatus.APPROVED)
                .build();


        JsonContent<BookingResponseDto> result = json.write(bookingDto);


        assertThat(result).extractingJsonPathStringValue("$.start")
                .isEqualTo("2024-01-15T14:30:45");
        assertThat(result).extractingJsonPathStringValue("$.end")
                .isEqualTo("2024-01-18T14:30:45");
    }
}