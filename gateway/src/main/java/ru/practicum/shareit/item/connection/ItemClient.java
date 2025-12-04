package ru.practicum.shareit.item.connection;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ItemClient {
    private final RestTemplate restTemplate;
    @Value("${shareit-server.url}")
    private String serverUrl;

    public ResponseEntity<Object> addNewItem(Long userId, ItemRequestDto itemDto) {
        HttpHeaders headers = createHeaders(userId);
        HttpEntity<ItemRequestDto> requestEntity = new HttpEntity<>(itemDto, headers);

        return restTemplate.exchange(
                serverUrl + "/items",
                HttpMethod.POST,
                requestEntity,
                Object.class
        );
    }

    public ResponseEntity<Object> getItemById(Long userId, Long itemId) {
        HttpHeaders headers = createHeaders(userId);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                serverUrl + "/items/" + itemId,
                HttpMethod.GET,
                requestEntity,
                Object.class
        );
    }

    public ResponseEntity<Object> getAllItems(Long userId) {
        HttpHeaders headers = createHeaders(userId);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(
                serverUrl + "/items",
                HttpMethod.GET,
                requestEntity,
                Object.class
        );
    }

    public ResponseEntity<Object> updateItem(Long userId, ItemRequestDto itemDto, Long itemId) {
        HttpHeaders headers = createHeaders(userId);
        HttpEntity<ItemRequestDto> requestEntity = new HttpEntity<>(itemDto, headers);
        return restTemplate.exchange(
                serverUrl + "/items/" + itemId,
                HttpMethod.PATCH,
                requestEntity,
                Object.class
        );
    }

    public ResponseEntity<Object> deleteItem(Long userId, Long itemId) {
        HttpHeaders headers = createHeaders(userId);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(
                serverUrl + "/items/" + itemId,
                HttpMethod.DELETE,
                requestEntity,
                Object.class
        );
    }

    public ResponseEntity<Object> searchItem(Long userId, String text, Integer from, Integer size) {
        HttpHeaders headers = createHeaders(userId);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        Map<String, Object> parameters = Map.of(
                "text", text,
                "from", from,
                "size", size
        );

        return restTemplate.exchange(
                serverUrl + "/items/search?text={text}&from={from}&size={size}",
                HttpMethod.GET,
                requestEntity,
                Object.class,
                parameters
        );
    }

    public ResponseEntity<Object> addComment(Long userId, Long itemId, CommentRequestDto commentDto) {
        HttpHeaders headers = createHeaders(userId);
        HttpEntity<CommentRequestDto> requestEntity = new HttpEntity<>(commentDto, headers);
        return restTemplate.exchange(
                serverUrl + "/items/" + itemId + "/comment",
                HttpMethod.POST,
                requestEntity,
                Object.class
        );
    }


    private HttpHeaders createHeaders(Long userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (userId != null) {
            headers.set("X-Sharer-User-Id", String.valueOf(userId));
        }
        return headers;
    }
}
