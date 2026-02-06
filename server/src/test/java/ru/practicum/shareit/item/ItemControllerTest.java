package ru.practicum.shareit.item;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mvc;

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";



    @Test
    void getUserItems_shouldReturnListOfItems() throws Exception {

        ItemResponseDto item1 = ItemResponseDto.builder()
                .id(1L)
                .name("Item 1")
                .description("Description 1")
                .available(true)
                .build();

        ItemResponseDto item2 = ItemResponseDto.builder()
                .id(2L)
                .name("Item 2")
                .description("Description 2")
                .available(false)
                .build();

        when(itemService.getAllItems(1L))
                .thenReturn(List.of(item1, item2));

        mvc.perform(get("/items")
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Item 1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Item 2")));
    }



    @Test
    void searchItems_withEmptyText_shouldReturnEmptyList() throws Exception {
        mvc.perform(get("/items/search")
                        .param("text", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}