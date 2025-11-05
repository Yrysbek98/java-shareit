package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;


public class ItemMapper {
    public static Item toEntity(ItemRequestDto dto) {
        if (dto == null) {
            return null;
        }
        return new Item(dto.getName(), dto.getDescription(), dto.getAvailable());
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
                item.getOwnerId()
        );
    }
}
