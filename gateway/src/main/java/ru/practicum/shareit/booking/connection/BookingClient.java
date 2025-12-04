package ru.practicum.shareit.booking.connection;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.enums.BookingState;

@Service
@RequiredArgsConstructor
public class BookingClient {
    private final RestTemplate restTemplate;

    @Value("${shareit-server.url}")
    private String serverUrl;

    public ResponseEntity<Object> addNewReservation(Long userId, BookingRequestDto dto) {
        HttpHeaders headers = createHeaders(userId);
        HttpEntity<BookingRequestDto> requestEntity = new HttpEntity<>(dto, headers);

        return restTemplate.exchange(
                serverUrl + "/bookings",
                HttpMethod.POST,
                requestEntity,
                Object.class
        );
    }

    public ResponseEntity<Object> changeState(Long userId, Long bookingId, Boolean approved) {
        HttpHeaders headers = createHeaders(userId);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                serverUrl + "/bookings/" + bookingId + "?approved=" + approved,
                HttpMethod.PATCH,
                requestEntity,
                Object.class
        );
    }

    public ResponseEntity<Object> getBookingById(Long userId, Long bookingId) {
        HttpHeaders headers = createHeaders(userId);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                serverUrl + "/bookings/" + bookingId,
                HttpMethod.GET,
                requestEntity,
                Object.class
        );
    }

    public ResponseEntity<Object> getBookingsByUser(Long userId, BookingState state) {
        HttpHeaders headers = createHeaders(userId);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                serverUrl + "/bookings?state=" + state,
                HttpMethod.GET,
                requestEntity,
                Object.class
        );
    }

    public ResponseEntity<Object> getBookingsByOwner(Long userId, BookingState state) {
        HttpHeaders headers = createHeaders(userId);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                serverUrl + "/bookings/owner?state=" + state,
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
