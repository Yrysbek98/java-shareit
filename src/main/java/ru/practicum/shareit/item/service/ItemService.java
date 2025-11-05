package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.util.List;

public interface ItemService {

    ItemResponseDto getItemById(Long id);

    List<ItemResponseDto> getAllItems(Long userId);

    ItemResponseDto addNewItem(Long userId, ItemRequestDto itemDto);

    ItemResponseDto updateItem(Long userId, Long id, ItemRequestDto itemDto);

    void deleteItemById(Long id);

    List<ItemResponseDto> searchItem(String name);
}
