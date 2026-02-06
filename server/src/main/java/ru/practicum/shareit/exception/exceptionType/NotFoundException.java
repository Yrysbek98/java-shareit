package ru.practicum.shareit.exception.exceptionType;


import ru.practicum.shareit.exception.AppException;

public class NotFoundException extends AppException {
    public NotFoundException(String message) {
        super(message);
    }
}
