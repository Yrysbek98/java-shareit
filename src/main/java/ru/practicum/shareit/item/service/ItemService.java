package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto getItemById(Long id);

    List<ItemDto> getAllItems(Long userId);

    ItemDto addNewItem(Long userId, ItemDto itemDto);

    ItemDto updateItem(Long userId, Long id, ItemDto itemDto);

    void deleteItemById(Long id);

    List<ItemDto> searchItem(String name);
}
