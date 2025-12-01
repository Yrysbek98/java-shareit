package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.BookingShortDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;


public class ItemMapper {
    public static Item toEntity(ItemRequestDto dto) {
        if (dto == null) {
            return null;
        }

        Item item = new Item();
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());
        item.setRequestId(dto.getRequestId());

        return item;
    }

    public static ItemResponseDto toDto(Item item) {
        if (item == null) {
            return null;
        }

        return new ItemResponseDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwnerId(),
                null,
                null,
                List.of(),
                item.getRequestId()
        );
    }

    public static ItemResponseDto toDto(Item item, BookingShortDto lastBooking,
                                        BookingShortDto nextBooking, List<CommentResponseDto> comments) {
        if (item == null) {
            return null;
        }

        return new ItemResponseDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwnerId(),
                lastBooking,
                nextBooking,
                comments != null ? comments : List.of(),
                item.getRequestId()
        );
    }
}
