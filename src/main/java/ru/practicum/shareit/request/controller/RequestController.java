package ru.practicum.shareit.request.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.ResponseDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.service.RequestService;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;
    @PostMapping()
    public ResponseDto addNewItem(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @Valid @RequestBody RequestDto itemDto) {
        return requestService.addNewRequest(itemDto);
    }

    @GetMapping("/{requestId}")
    public ResponseDto getBookingById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long requestId) {
        return requestService.getRequestById( requestId);
    }
}
