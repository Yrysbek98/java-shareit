package ru.practicum.shareit.request.connection;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ru.practicum.shareit.request.dto.RequestDto;

@Service
@RequiredArgsConstructor
public class RequestClient {
    private final RestTemplate restTemplate;
    @Value("${shareit-server.url}")
    private String serverUrl;

    public ResponseEntity<Object> addNewRequest(Long userId, RequestDto requestDto) {
        HttpHeaders headers = createHeaders(userId);
        HttpEntity<RequestDto> requestEntity = new HttpEntity<>(requestDto, headers);

        return restTemplate.exchange(
                serverUrl + "/requests",
                HttpMethod.POST,
                requestEntity,
                Object.class
        );
    }

    public ResponseEntity<Object> getUserRequests(Long userId) {
        HttpHeaders headers = createHeaders(userId);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                serverUrl + "/requests",
                HttpMethod.GET,
                requestEntity,
                Object.class
        );
    }

    public ResponseEntity<Object> getAllRequests(Long userId) {
        HttpHeaders headers = createHeaders(userId);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(
                serverUrl + "/requests/all",
                HttpMethod.GET,
                requestEntity,
                Object.class
        );
    }

    public ResponseEntity<Object> getRequestById(Long userId, Long requestId) {
        HttpHeaders headers = createHeaders(userId);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                serverUrl + "/requests/" + requestId,
                HttpMethod.GET,
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
