package ru.practicum.shareit.exception.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.enums.BookingState;
import ru.practicum.shareit.exception.exceptionType.ValidationException;

@Component
public class StringToBookingStateConverter implements Converter<String, BookingState> {
    @Override
    public BookingState convert(String source) {
        try {
            return BookingState.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Unknown state: " + source);
        }
    }
}
