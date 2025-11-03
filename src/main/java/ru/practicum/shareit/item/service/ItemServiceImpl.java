package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemValidationException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{
    private ConcurrentHashMap<Long, Item> items = new ConcurrentHashMap<>();

    private final AtomicLong idGenerator = new AtomicLong(0);
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
        if (itemDto == null){
            throw  new ItemValidationException("Данные товара не могут быть пустыми");
        }
        Item item = ItemMapper.toEntity(itemDto);
        long id = idGenerator.getAndIncrement();
        item.setId(id);
        items.put(id, item);
        return ItemMapper.toDto(item);
    }

    @Override
    public ItemDto updateItem(Long id, ItemDto itemDto) {
        Item item = items.get(id);
        items.put(item.getId(), item);
        return ItemMapper.toDto(item);
    }

    @Override
    public void deleteItemById(Long id) {
        items.remove(id);
    }
}
