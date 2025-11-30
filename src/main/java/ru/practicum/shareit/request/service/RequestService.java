package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.ResponseDto;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

public interface RequestService {

    ResponseDto addNewRequest(RequestDto request);

    ResponseDto getRequestById(Long requestId);

    List<ResponseDto> getAllRequests();
}
