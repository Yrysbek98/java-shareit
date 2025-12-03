package ru.practicum.shareit.user.connection;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.user.dto.UserRequestDto;

@Service
@RequiredArgsConstructor
public class UserClient {
    private final RestTemplate restTemplate;
    @Value("${shareit-server.url}")
    private String serverUrl;

    public ResponseEntity<Object> getUserById(Long userId) {
        HttpHeaders headers = createHeaders(null);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                serverUrl + "/users/" + userId,
                HttpMethod.GET,
                requestEntity,
                Object.class
        );
    }

    public ResponseEntity<Object> getAllUsers() {
        HttpHeaders headers = createHeaders(null);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(
                serverUrl + "/users",
                HttpMethod.GET,
                requestEntity,
                Object.class
        );
    }

    public ResponseEntity<Object> addNewUser(UserRequestDto userDto) {
        HttpHeaders headers = createHeaders(null);
        HttpEntity<UserRequestDto> requestEntity = new HttpEntity<>(userDto, headers);

        return restTemplate.exchange(
                serverUrl + "/users",
                HttpMethod.POST,
                requestEntity,
                Object.class
        );
    }

    public ResponseEntity<Object> updateUser(Long userId, UserRequestDto userDto) {
        HttpHeaders headers = createHeaders(null);
        HttpEntity<UserRequestDto> requestEntity = new HttpEntity<>(userDto, headers);
        return restTemplate.exchange(
                serverUrl + "/users/" + userId,
                HttpMethod.PATCH,
                requestEntity,
                Object.class
        );
    }

    public ResponseEntity<Object> deleteUser(Long userId) {
        HttpHeaders headers = createHeaders(null);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        HttpEntity<Void> requestEntity2;
        return restTemplate.exchange(
                serverUrl + "/users/" + userId,
                HttpMethod.DELETE,
                requestEntity,
                Object.class
        );
    }

    private HttpHeaders createHeaders(Long userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Sharer-User-Id", String.valueOf(userId));
        return headers;
    }
}
