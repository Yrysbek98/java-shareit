package ru.practicum.shareit.item.controller;

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
    public ItemDto addNewItem(@RequestBody ItemDto itemDto){
        return ItemService.addNewItem(itemDto);
    }

    @PatchMapping
    public ItemDto updateItem(@RequestBody ItemDto itemDto){
        return ItemService.updateItem(itemDto);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id){
        ItemService.deleteItemById(id);
    }

    @ExceptionHandler(ItemValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(ItemValidationException ex) {
        ErrorResponse errorResponse = ex.toResponse();
        return new ResponseEntity<>(errorResponse, errorResponse.httpStatusCode());
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ItemNotFoundException ex) {
        ErrorResponse errorResponse = ex.toResponse();
        return new ResponseEntity<>(errorResponse, errorResponse.httpStatusCode());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleServerExceptions(AbstractDtoException exception) {
        ErrorResponse errorResponse = exception.toResponse();
        return new ResponseEntity<>(errorResponse, errorResponse.httpStatusCode());
    }
}
