package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{id}")
    public ItemResponseDto getItemById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long id) {
        return itemService.getItemById(userId, id);
    }

    @GetMapping()
    public List<ItemResponseDto> getAllItems(
            @RequestHeader("X-Sharer-User-Id") Long userId
    ) {
        return itemService.getAllItems(userId);
    }

    @PostMapping()
    public ItemResponseDto addNewItem(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @Valid @RequestBody ItemRequestDto itemDto) {
        return itemService.addNewItem(userId, itemDto);
    }

    @PatchMapping("/{id}")
    public ItemResponseDto updateItem(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long id,
            @RequestBody ItemRequestDto itemDto) {
        return itemService.updateItem(userId, id, itemDto);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id) {
        itemService.deleteItemById(id);
    }

    @GetMapping("/search")
    public List<ItemResponseDto> searchItem(@RequestParam String text) {
        return itemService.searchItem(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto addComment(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId,
            @Valid @RequestBody CommentRequestDto commentDto) {
        return itemService.addComment(userId, itemId, commentDto);
    }
}
