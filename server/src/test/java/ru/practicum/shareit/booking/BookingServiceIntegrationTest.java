package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceIntegrationTest {

    private final BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService;

    @Test
    void createBooking_shouldCreateAndReturnBooking() {

        UserRequestDto owner = new UserRequestDto("Owner", "owner@example.com");
        UserResponseDto createdOwner = userService.addNewUser(owner);

        UserRequestDto booker = new UserRequestDto("Booker", "booker@example.com");
        UserResponseDto createdBooker = userService.addNewUser(booker);

        ItemRequestDto itemDto = ItemRequestDto.builder()
                .name("Дрель")
                .description("Простая дрель")
                .available(true)
                .build();
        ItemResponseDto item = itemService.addNewItem(createdOwner.getId(), itemDto);

        BookingRequestDto requestDto = BookingRequestDto.builder()
                .itemId(item.getId())
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();


        BookingResponseDto booking = bookingService.addNewReservation(createdBooker.getId(), requestDto);


        assertThat(booking.getId(), notNullValue());
        assertThat(booking.getStatus(), equalTo(BookingStatus.WAITING));
        assertThat(booking.getBooker().getId(), equalTo(createdBooker.getId()));
        assertThat(booking.getItem().getId(), equalTo(item.getId()));
    }

    @Test
    void approveBooking_shouldChangeStatusToApproved() {

        UserRequestDto owner = new UserRequestDto("Owner", "owner@example.com");
        UserResponseDto createdOwner = userService.addNewUser(owner);

        UserRequestDto booker = new UserRequestDto("Booker", "booker@example.com");
        UserResponseDto createdBooker = userService.addNewUser(booker);

        ItemRequestDto itemDto = ItemRequestDto.builder()
                .name("Дрель")
                .description("Простая дрель")
                .available(true)
                .build();
        ItemResponseDto item = itemService.addNewItem(createdOwner.getId(), itemDto);

        BookingRequestDto requestDto = BookingRequestDto.builder()
                .itemId(item.getId())
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        BookingResponseDto booking = bookingService.addNewReservation(createdBooker.getId(), requestDto);

        BookingResponseDto approvedBooking = bookingService.changeState(
                createdOwner.getId(), booking.getId(), true);

        assertThat(approvedBooking.getStatus(), equalTo(BookingStatus.APPROVED));
        assertThat(approvedBooking.getId(), equalTo(booking.getId()));
    }
}
