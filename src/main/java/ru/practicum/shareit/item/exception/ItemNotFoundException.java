package ru.practicum.shareit.item.exception;

import org.springframework.http.HttpStatus;
import ru.practicum.shareit.exception.AbstractDtoException;

public class ItemNotFoundException extends AbstractDtoException {
    public ItemNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
