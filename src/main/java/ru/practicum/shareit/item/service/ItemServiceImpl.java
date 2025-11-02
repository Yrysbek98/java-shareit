package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{
    private HashMap<Long, Item> items = new HashMap<>();
    private Long nextId = 1L;
    @Override
    public ItemDto getItemById(Long id) {
        return ItemMapper.toDto(items.get(id));
    }

    @Override
    public List<ItemDto> getAllItems() {
        return items.values().stream()
                .map(ItemMapper::toDto)
                .toList();
    }

    @Override
    public ItemDto addNewItem(ItemDto itemDto) {
        Item item = ItemMapper.toEntity(itemDto);
        items.put(nextId++, item);
        return ItemMapper.toDto(item);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto) {
        Item item = ItemMapper.toEntity(itemDto);
        items.put(item.getItemId(), item);
        return ItemMapper.toDto(item);
    }

    @Override
    public void deleteItemById(Long id) {
        items.remove(id);
    }
}
