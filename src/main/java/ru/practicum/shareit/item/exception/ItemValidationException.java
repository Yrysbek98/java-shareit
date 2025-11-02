package ru.practicum.shareit.item.exception;

import org.springframework.http.HttpStatus;
import ru.practicum.shareit.exception.AbstractDtoException;

public class ItemValidationException extends AbstractDtoException {
    public ItemValidationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
