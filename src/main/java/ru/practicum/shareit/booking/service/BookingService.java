package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;


public interface BookingService {
    BookingResponseDto addNewReservation(Long userId, BookingRequestDto reservation);

    BookingResponseDto changeState(Long userId, Long bookingId,Boolean approved);

}
