package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.ResponseDto;

import java.util.List;

public interface RequestService {

    ResponseDto addNewRequest(Long userId, RequestDto request);

    ResponseDto getRequestById(Long userId, Long requestId);

    List<ResponseDto> getUserRequests(Long userId);

    List<ResponseDto> getAllRequests(Long userId);
}
