package ru.practicum.shareit.exception.exceptionType;

import ru.practicum.shareit.exception.AppException;

public class ValidationException extends AppException {
    public ValidationException(String message) {
        super(message);
    }
}
