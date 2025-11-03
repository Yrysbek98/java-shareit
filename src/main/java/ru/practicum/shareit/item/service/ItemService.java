package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto getItemById(Long id);

    List<ItemDto> getAllItems();

    ItemDto addNewItem(ItemDto itemDto);

    ItemDto updateItem(Long id, ItemDto itemDto);

    void deleteItemById(Long id);
}
