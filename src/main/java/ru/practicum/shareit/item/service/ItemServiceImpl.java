package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.ItemValidationException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{
    private ConcurrentHashMap<Long, Item> items = new ConcurrentHashMap<>();
    private final UserService userService;

    private final AtomicLong idGenerator = new AtomicLong(0);
    @Override
    public ItemDto getItemById(Long id) {
        Item item = items.get(id);
        if (item == null) {
            throw new ItemNotFoundException("Предмет не найден");
        }
        return ItemMapper.toDto(item);
    }

    @Override
    public List<ItemDto> getAllItems(Long userId) {
        return items.values().stream()
                .filter(item -> item.getOwnerId().equals(userId))
                .map(ItemMapper::toDto)
                .toList();
    }

    @Override
    public ItemDto addNewItem(Long userId, ItemDto itemDto) {
        if (itemDto == null){
            throw  new ItemValidationException("Данные товара не могут быть пустыми");
        }
        User user = UserMapper.toEntity(userService.getUserById(userId));
        if (user == null){
            throw new UserNotFoundException("Пользователь не найден");
        }

        Item item = ItemMapper.toEntity(itemDto);
        item.setOwnerId(userId);
        long id = idGenerator.getAndIncrement();
        item.setId(id);
        items.put(id, item);
        return ItemMapper.toDto(item);
    }

    @Override
    public ItemDto updateItem(Long userId, Long id, ItemDto itemDto) {
        Item item = items.get(id);
        if (item == null){
            throw new ItemNotFoundException("Такой Item не был найден");
        }
        User user = UserMapper.toEntity(userService.getUserById(userId));
        if (user == null){
            throw new UserNotFoundException("Пользователь не найден");
        }
        if (item.getName() != null){
            item.setName(itemDto.getName());
        }
        if (item.getDescription() != null){
            item.setDescription(itemDto.getDescription());
        }
        if (item.getAvailable() != null){
            item.setAvailable(itemDto.getAvailable());
        }
        items.put(id, item);
        return ItemMapper.toDto(item);
    }

    @Override
    public void deleteItemById(Long id) {
        items.remove(id);
    }
}
