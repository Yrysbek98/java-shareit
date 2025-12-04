package ru.practicum.shareit.exception.exceptionType;


import ru.practicum.shareit.exception.AppException;

public class ConflictException extends AppException {
    public ConflictException(String message) {
        super(message);
    }
}
