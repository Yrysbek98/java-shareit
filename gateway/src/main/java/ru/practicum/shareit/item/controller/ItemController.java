package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.connection.ItemClient;
import ru.practicum.shareit.item.dto.CommentRequestDto;

import ru.practicum.shareit.item.dto.ItemRequestDto;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItemById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long id) {
        return itemClient.getItemById(userId, id);
    }

    @GetMapping()
    public ResponseEntity<Object> getAllItems(
            @RequestHeader("X-Sharer-User-Id") Long userId
    ) {
        return itemClient.getAllItems(userId);
    }

    @PostMapping()
    public ResponseEntity<Object> addNewItem(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @Valid @RequestBody ItemRequestDto itemDto) {
        return itemClient.addNewItem(userId, itemDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateItem(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long id,
            @RequestBody ItemRequestDto itemDto) {
        return itemClient.updateItem(userId, itemDto, id);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long id) {
        itemClient.deleteItem(userId, id);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam String text,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {

        return itemClient.searchItem(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId,
            @Valid @RequestBody CommentRequestDto commentDto) {
        return itemClient.addComment(userId, itemId, commentDto);
    }
}
