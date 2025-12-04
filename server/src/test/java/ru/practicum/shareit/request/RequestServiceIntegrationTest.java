package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.ResponseDto;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class RequestServiceIntegrationTest {

    private final RequestService requestService;
    private final UserService userService;

    @Test
    void createRequest_shouldCreateAndReturnRequest() {

        UserRequestDto requester = new UserRequestDto("Requester", "requester@example.com");

        UserResponseDto createdUser = userService.addNewUser(requester);

        RequestDto requestDto = RequestDto.builder()
                .description("Нужна дрель")
                .build();


        ResponseDto createdRequest = requestService.addNewRequest(
                createdUser.getId(), requestDto);


        assertThat(createdRequest.getId(), notNullValue());
        assertThat(createdRequest.getDescription(), equalTo("Нужна дрель"));
        assertThat(createdRequest.getCreated(), notNullValue());
    }

    @Test
    void getUserRequests_shouldReturnAllUserRequests() {
        UserRequestDto requester = new UserRequestDto("Requester", "requester@example.com");

        UserResponseDto createdUser = userService.addNewUser(requester);

        RequestDto request1 = RequestDto.builder()
                .description("Нужна дрель")
                .build();

        RequestDto request2 = RequestDto.builder()
                .description("Нужна отвертка")
                .build();

        requestService.addNewRequest(createdUser.getId(), request1);
        requestService.addNewRequest(createdUser.getId(), request2);

        List<ResponseDto> requests = requestService.getUserRequests(createdUser.getId());

        assertThat(requests, hasSize(2));
        assertThat(requests.get(0).getDescription(), either(is("Нужна дрель")).or(is("Нужна отвертка")));
        assertThat(requests.get(1).getDescription(), either(is("Нужна дрель")).or(is("Нужна отвертка")));
    }


    @Test
    void getUserRequests_shouldReturnEmptyListForUserWithoutRequests() {
        UserRequestDto requester = new UserRequestDto("Requester", "requester@example.com");

        UserResponseDto createdUser = userService.addNewUser(requester);

        List<ResponseDto> requests = requestService.getUserRequests(createdUser.getId());

        assertThat(requests, empty());
    }
}
