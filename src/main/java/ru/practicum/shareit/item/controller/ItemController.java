package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.AbstractDtoException;
import ru.practicum.shareit.exception.ErrorResponse;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.ItemValidationException;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final  ItemService ItemService;
    @GetMapping("/{id}")
    public ItemDto getItemById(@PathVariable Long id){
        return ItemService.getItemById(id);
    }

    @GetMapping()
    public List<ItemDto> getAllItems(){
        return ItemService.getAllItems();
    }

    @PostMapping()
    public ItemDto addNewItem(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @Valid @RequestBody ItemDto itemDto){
        return ItemService.addNewItem(userId, itemDto);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(
            @PathVariable Long id,
            @RequestBody ItemDto itemDto){
        return ItemService.updateItem(id, itemDto);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id){
        ItemService.deleteItemById(id);
    }


    @ExceptionHandler(AbstractDtoException.class)
    public ResponseEntity<ErrorResponse> handleDtoExceptions(AbstractDtoException ex) {
        ErrorResponse errorResponse = ex.toResponse();
        return new ResponseEntity<>(errorResponse, errorResponse.httpStatusCode());
    }

}
