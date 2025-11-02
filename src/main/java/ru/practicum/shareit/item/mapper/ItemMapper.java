package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;


public class ItemMapper {
    public static Item toEntity(ItemDto dto) {
        Item item = new Item();
        item.setItemName(dto.getItemName());
        item.setItemDescription(dto.getItemDescription());
        item.setOwnerId(dto.getOwnerId());
        return item;
    }

    public static ItemDto toDto(Item item) {
        return new ItemDto(item.getItemId(), item.getItemName(), item.getItemDescription(), item.getOwnerId());
    }
}
