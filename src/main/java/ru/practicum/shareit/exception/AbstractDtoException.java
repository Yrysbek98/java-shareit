package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;

public abstract class AbstractDtoException extends RuntimeException {
    private HttpStatus httpStatus;

    public AbstractDtoException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public ErrorResponse toResponse() {
        return new ErrorResponse(getMessage(), httpStatus);
    }
}
