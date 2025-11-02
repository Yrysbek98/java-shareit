package ru.practicum.shareit.item.controller;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {
    private ItemService ItemService;
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

}
