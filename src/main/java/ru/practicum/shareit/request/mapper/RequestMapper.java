package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.ResponseDto;
import ru.practicum.shareit.request.model.Request;

import java.time.LocalDateTime;
import java.util.List;

public class RequestMapper {

    public static Request toEntity(RequestDto dto) {
        if (dto == null) {
            return null;
        }

        Request request = new Request();
        request.setDescription(dto.getDescription());
        request.setCreated(LocalDateTime.now());

        return request;
    }

    public static ResponseDto toDto(Request request) {
        if (request == null) {
            return null;
        }

        return new ResponseDto(
                request.getId(),
                request.getDescription(),
                request.getCreated(),
                ItemMapper.toDto(request.getItem())

        );
    }
}
