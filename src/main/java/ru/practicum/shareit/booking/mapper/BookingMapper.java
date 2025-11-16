package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;


public class BookingMapper {
    public static Booking toEntity(BookingRequestDto dto) {
        if (dto == null) {
            return null;
        }

        Booking booking = new Booking();
        booking.setItemId(dto.getItemId());
        booking.setStart(dto.getStart());
        booking.setEnd(dto.getEnd());

        return booking;
    }

    public static BookingResponseDto toDto(Booking booking) {
        if (booking == null) {
            return null;
        }

        return new BookingResponseDto(
                booking.getId(),
                booking.getItemId(),
                booking.getBookerId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus()
        );
    }
}
