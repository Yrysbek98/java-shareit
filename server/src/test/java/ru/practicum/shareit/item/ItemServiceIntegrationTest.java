package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import jakarta.persistence.EntityManager;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceIntegrationTest {

    private final EntityManager em;
    private final ItemService itemService;
    private final UserRepository userRepository;

    @Test
    void getUserItems_shouldReturnAllUserItems() {
        User owner = new User();
        owner.setName("Test User");
        owner.setEmail("test@example.com");
        owner = userRepository.save(owner);

        ItemRequestDto itemDto1 = ItemRequestDto.builder()
                .name("Item 1")
                .description("Description 1")
                .available(true)
                .build();

        ItemRequestDto itemDto2 = ItemRequestDto.builder()
                .name("Item 2")
                .description("Description 2")
                .available(false)
                .build();

        itemService.addNewItem(owner.getId(), itemDto1);
        itemService.addNewItem(owner.getId(), itemDto2);


        List<ItemResponseDto> userItems = itemService.getAllItems(owner.getId());


        assertThat(userItems, hasSize(2));
        assertThat(userItems.get(0).getName(), equalTo("Item 1"));
        assertThat(userItems.get(1).getName(), equalTo("Item 2"));
        assertThat(userItems.get(0).getAvailable(), is(true));
        assertThat(userItems.get(1).getAvailable(), is(false));
    }

    @Test
    void searchItems_shouldReturnMatchingItems() {

        User owner = new User();
        owner.setName("Owner");
        owner.setEmail("owner@example.com");
        owner = userRepository.save(owner);

        ItemRequestDto itemDto1 = ItemRequestDto.builder()
                .name("Дрель")
                .description("Аккумуляторная дрель")
                .available(true)
                .build();

        ItemRequestDto itemDto2 = ItemRequestDto.builder()
                .name("Отвертка")
                .description("Простая отвертка")
                .available(true)
                .build();

        itemService.addNewItem(owner.getId(), itemDto1);
        itemService.addNewItem(owner.getId(), itemDto2);


        List<ItemResponseDto> foundItems = itemService.searchItem("дрель", 1, 10);


        assertThat(foundItems, hasSize(1));
        assertThat(foundItems.get(0).getName(), containsStringIgnoringCase("дрель"));
    }
}
