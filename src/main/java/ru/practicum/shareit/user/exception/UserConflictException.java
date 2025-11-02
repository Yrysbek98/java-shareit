package ru.practicum.shareit.user.exception;

import org.springframework.http.HttpStatus;
import ru.practicum.shareit.exception.AbstractDtoException;

public class UserConflictException extends AbstractDtoException {
    public UserConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
