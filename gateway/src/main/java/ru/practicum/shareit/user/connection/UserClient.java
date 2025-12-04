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
        return restTemplate.exchange(
                serverUrl + "/users/" + userId,
                HttpMethod.GET,
                null,  // без headers и body
                Object.class
        );
    }

    public ResponseEntity<Object> getAllUsers() {
        return restTemplate.exchange(
                serverUrl + "/users",
                HttpMethod.GET,
                null,
                Object.class

        );
    }

    public ResponseEntity<Object> addNewUser(UserRequestDto userDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserRequestDto> requestEntity = new HttpEntity<>(userDto, headers);

        return restTemplate.exchange(
                serverUrl + "/users",
                HttpMethod.POST,
                requestEntity,
                Object.class
        );
    }

    public ResponseEntity<Object> updateUser(Long userId, UserRequestDto userDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserRequestDto> requestEntity = new HttpEntity<>(userDto, headers);

        return restTemplate.exchange(
                serverUrl + "/users/" + userId,
                HttpMethod.PATCH,
                requestEntity,
                Object.class
        );
    }

    public ResponseEntity<Object> deleteUser(Long userId) {
        return restTemplate.exchange(
                serverUrl + "/users/" + userId,
                HttpMethod.DELETE,
                null,
                Object.class
        );
    }
}
