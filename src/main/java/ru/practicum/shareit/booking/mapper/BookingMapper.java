package ru.practicum.shareit.booking.mapper;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;


@RequiredArgsConstructor
public class BookingMapper {
    public static Booking toEntity(BookingRequestDto dto, Item item, User booker) {
        if (dto == null) {
            return null;
        }

        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStart(dto.getStart());
        booking.setEnd(dto.getEnd());
        booking.setStatus(BookingStatus.WAITING);
        return booking;
    }

    public static BookingResponseDto toDto(Booking booking) {
        if (booking == null) {
            return null;
        }

        return new BookingResponseDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                UserMapper.toDto(booking.getBooker()),
                ItemMapper.toDto(booking.getItem()),
                booking.getStatus()
        );
    }
}
