package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.exceptionType.NotFoundException;

import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import ru.practicum.shareit.user.repository.UserRepository;


import java.util.*;


@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemResponseDto getItemById(Long id) {
        Optional<Item> item = itemRepository.findById(id);
        if (item.isEmpty()) {
            throw new NotFoundException("Вещь с id = " + id + " не найдена");
        }
        return ItemMapper.toDto(item.get());
    }

    @Override
    public List<ItemResponseDto> getAllItems(Long userId) {
        return itemRepository.findAllByOwnerId(userId).stream()
                .map(ItemMapper::toDto)
                .toList();
    }

    @Override
    public ItemResponseDto addNewItem(Long userId, ItemRequestDto itemDto) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }

        Item item = ItemMapper.toEntity(itemDto);

        item.setOwnerId(userId);

        Item saved = itemRepository.save(item);
        return ItemMapper.toDto(saved);
    }

    @Override
    public ItemResponseDto updateItem(Long userId, Long id, ItemRequestDto itemDto) {

        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Вещь с id = " + id + " не найдена"));
        if (!item.getOwnerId().equals(userId)) {
            throw new NotFoundException("Вещь с id = " + id + " не найдена для пользователя с id = " + userId);
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

        Item updated = itemRepository.save(item);
        return ItemMapper.toDto(updated);
    }

    @Override
    public void deleteItemById(Long id) {
        Optional<Item> item = itemRepository.findById(id);
        if (item.isEmpty()) {
            throw new NotFoundException("Вещь с id = " + id + " не найдена");
        }
        itemRepository.deleteById(id);
    }

    @Override
    public List<ItemResponseDto> searchItem(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        List<Item> items = itemRepository
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(text, text);

        return items.stream()
                .filter(Item::getAvailable)
                .map(ItemMapper::toDto)
                .toList();
    }

}
