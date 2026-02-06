package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @Test
    void createBooking_shouldReturnCreatedBooking() throws Exception {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        BookingRequestDto requestDto = BookingRequestDto.builder()
                .itemId(1L)
                .start(start)
                .end(end)
                .build();

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

        BookingResponseDto responseDto = BookingResponseDto.builder()
                .id(1L)
                .start(start)
                .end(end)
                .item(item)
                .booker(booker)
                .status(BookingStatus.WAITING)
                .build();

        when(bookingService.addNewReservation(eq(1L), any(BookingRequestDto.class)))
                .thenReturn(responseDto);

        mvc.perform(post("/bookings")
                        .header(USER_ID_HEADER, 1L)
                        .content(mapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.status", is("WAITING")))
                .andExpect(jsonPath("$.item.id", is(1)))
                .andExpect(jsonPath("$.booker.id", is(1)));
    }

    @Test
    void approveBooking_shouldReturnApprovedBooking() throws Exception {
        UserResponseDto owner = UserResponseDto.builder()
                .id(2L)
                .name("Owner")
                .email("owner@example.com")
                .build();

        ItemResponseDto item = ItemResponseDto.builder()
                .id(1L)
                .name("Дрель")
                .description("Простая дрель")
                .available(true)
                .build();

        BookingResponseDto responseDto = BookingResponseDto.builder()
                .id(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .item(item)
                .booker(owner)
                .status(BookingStatus.APPROVED)
                .build();

        when(bookingService.changeState(eq(2L), eq(1L), eq(true)))
                .thenReturn(responseDto);

        mvc.perform(patch("/bookings/1")
                        .header(USER_ID_HEADER, 2L)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.status", is("APPROVED")));
    }

    @Test
    void rejectBooking_shouldReturnRejectedBooking() throws Exception {
        UserResponseDto owner = UserResponseDto.builder()
                .id(2L)
                .name("Owner")
                .email("owner@example.com")
                .build();

        ItemResponseDto item = ItemResponseDto.builder()
                .id(1L)
                .name("Дрель")
                .description("Простая дрель")
                .available(true)
                .build();

        BookingResponseDto responseDto = BookingResponseDto.builder()
                .id(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .item(item)
                .booker(owner)
                .status(BookingStatus.REJECTED)
                .build();

        when(bookingService.changeState(eq(2L), eq(1L), eq(false)))
                .thenReturn(responseDto);

        mvc.perform(patch("/bookings/1")
                        .header(USER_ID_HEADER, 2L)
                        .param("approved", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.status", is("REJECTED")));
    }

    @Test
    void getBookingById_shouldReturnBooking() throws Exception {
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

        BookingResponseDto responseDto = BookingResponseDto.builder()
                .id(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .item(item)
                .booker(booker)
                .status(BookingStatus.WAITING)
                .build();

        when(bookingService.getBookingById(eq(1L), eq(1L)))
                .thenReturn(responseDto);

        mvc.perform(get("/bookings/1")
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.status", is("WAITING")));
    }

}
