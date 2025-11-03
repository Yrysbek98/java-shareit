package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;


public class ItemMapper {
    public static Item toEntity(ItemDto dto) {
        if (dto == null){
            return  null;
        }
        return new Item(dto.getId(), dto.getName(), dto.getDescription(), dto.getAvailable());
    }

    public static ItemDto toDto(Item item) {
        if(item == null){
            return null;
        }
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable());
    }
}
