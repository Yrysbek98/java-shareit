package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemResponseForRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.ResponseDto;
import ru.practicum.shareit.request.model.Request;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class RequestMapper {

    public static Request toEntity(RequestDto dto, Long requesterId) {
        if (dto == null) {
            return null;
        }

        Request request = new Request();
        request.setDescription(dto.getDescription());
        request.setCreated(LocalDateTime.now());
        request.setRequesterId(requesterId);

        return request;
    }

    public static ResponseDto toDto(Request request, List<Item> items) {
        if (request == null) {
            return null;
        }

        List<ItemResponseForRequestDto> itemDtos = items != null
                ? items.stream()
                .map(item -> new ItemResponseForRequestDto(
                        item.getId(),
                        item.getName(),
                        item.getOwnerId()
                ))
                .collect(Collectors.toList())
                : List.of();

        return new ResponseDto(
                request.getId(),
                request.getDescription(),
                request.getCreated(),
                itemDtos
        );
    }
}
