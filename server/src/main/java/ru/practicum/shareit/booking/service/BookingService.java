package ru.practicum.shareit.booking.service;


import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.enums.BookingState;

import java.util.List;


public interface BookingService {

    BookingResponseDto addNewReservation(Long userId, BookingRequestDto reservation);

    BookingResponseDto changeState(Long userId, Long bookingId, Boolean approved);

    BookingResponseDto getBookingById(Long userId, Long bookingId);

    List<BookingResponseDto> getBookingsByUser(Long userId, BookingState state, Integer from, Integer size);

    List<BookingResponseDto> getBookingsByOwner(Long ownerId, BookingState state, Integer from, Integer size);

}
