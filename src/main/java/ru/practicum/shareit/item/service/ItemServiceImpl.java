package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.exceptionType.NotFoundException;
import ru.practicum.shareit.exception.exceptionType.ValidationException;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ConcurrentHashMap<Long, Item> items = new ConcurrentHashMap<>();
    private final UserService userService;

    private final AtomicLong idGenerator = new AtomicLong(0);

    @Override
    public ItemResponseDto getItemById(Long id) {
        Item item = items.get(id);
        if (item == null) {
            throw new NotFoundException("Предмет не найден");
        }
        return ItemMapper.toDto(item);
    }

    @Override
    public List<ItemResponseDto> getAllItems(Long userId) {
        if (items.isEmpty()) {
            return new ArrayList<>();
        }
        return items.values().stream()
                .filter(item -> item.getOwnerId().equals(userId))
                .map(ItemMapper::toDto)
                .toList();
    }

    @Override
    public ItemResponseDto addNewItem(Long userId, ItemRequestDto itemDto) {
        if (itemDto == null) {
            throw new ValidationException("Данные товара не могут быть пустыми");
        }
        User user = UserMapper.toEntity(userService.getUserById(userId));
        if (user == null) {
            throw new NotFoundException("Пользователь не найден");
        }

        Item item = ItemMapper.toEntity(itemDto);
        item.setOwnerId(userId);
        long id = idGenerator.getAndIncrement();
        item.setId(id);
        items.put(id, item);
        return ItemMapper.toDto(item);
    }

    @Override
    public ItemResponseDto updateItem(Long userId, Long id, ItemRequestDto itemDto) {
        Item item = items.get(id);
        if (item == null) {
            throw new NotFoundException("Такой Item не был найден");
        }
        User user = UserMapper.toEntity(userService.getUserById(userId));
        if (user == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        items.put(id, item);
        return ItemMapper.toDto(item);
    }

    @Override
    public void deleteItemById(Long id) {
        items.remove(id);
    }

    @Override
    public List<ItemResponseDto> searchItem(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        return items.values().stream()
                .filter(Objects::nonNull)
                .filter(item -> Boolean.TRUE.equals(item.getAvailable()))
                .filter(item ->
                        (item.getName() != null && item.getName().toLowerCase().contains(text.toLowerCase())) ||
                                (item.getDescription() != null && item.getDescription().toLowerCase().contains(text.toLowerCase()))
                )
                .map(ItemMapper::toDto)
                .toList();
    }


}
