package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.exceptionType.NotFoundException;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.ResponseDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.request.model.Request;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final  RequestRepository requestRepository;
    @Override
    public ResponseDto addNewRequest(RequestDto dto) {
        Request request = RequestMapper.toEntity(dto);
        requestRepository.save(request);

        return RequestMapper.toDto(request);
    }

    @Override
    public ResponseDto getRequestById(Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос с id = " + requestId + " не найдено"));
        return RequestMapper.toDto(request);
    }

    @Override
    public List<ResponseDto> getAllRequests() {
        return List.of();
    }

}
