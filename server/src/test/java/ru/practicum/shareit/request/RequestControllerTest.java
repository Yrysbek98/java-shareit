package ru.practicum.shareit.request;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.controller.RequestController;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.ResponseDto;
import ru.practicum.shareit.request.service.RequestService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RequestController.class)
 class RequestControllerTest {
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private RequestService requestService;

    @Autowired
    private MockMvc mvc;

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @Test
    void createRequest_shouldReturnCreatedRequest() throws Exception {
        ResponseDto inputDto = ResponseDto.builder()
                .description("Нужна дрель")
                .build();

        ResponseDto outputDto = ResponseDto.builder()
                .id(1L)
                .description("Нужна дрель")
                .created(LocalDateTime.now())
                .build();

        when(requestService.addNewRequest(eq(1L), any(RequestDto.class)))
                .thenReturn(outputDto);

        mvc.perform(post("/requests")
                        .header(USER_ID_HEADER, 1L)
                        .content(mapper.writeValueAsString(inputDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("Нужна дрель")))
                .andExpect(jsonPath("$.created").exists());
    }

    @Test
    void getUserRequests_shouldReturnListOfRequests() throws Exception {
        ResponseDto request1 = ResponseDto.builder()
                .id(1L)
                .description("Нужна дрель")
                .created(LocalDateTime.now())
                .build();

        ResponseDto request2 = ResponseDto.builder()
                .id(2L)
                .description("Нужна отвертка")
                .created(LocalDateTime.now())
                .build();

        when(requestService.getUserRequests(1L))
                .thenReturn(List.of(request1, request2));

        mvc.perform(get("/requests")
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].description", is("Нужна дрель")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].description", is("Нужна отвертка")));
    }

    @Test
    void getAllRequests_shouldReturnListOfRequests() throws Exception {
        ResponseDto request1 = ResponseDto.builder()
                .id(1L)
                .description("Нужна дрель")
                .created(LocalDateTime.now())
                .build();

        when(requestService.getAllRequests(eq(1L)))
                .thenReturn(List.of(request1));

        mvc.perform(get("/requests/all")
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].description", is("Нужна дрель")));
    }

    @Test
    void getRequestById_shouldReturnRequest() throws Exception {
        ResponseDto requestDto = ResponseDto.builder()
                .id(1L)
                .description("Нужна дрель")
                .created(LocalDateTime.now())
                .build();

        when(requestService.getRequestById(eq(1L), eq(1L)))
                .thenReturn(requestDto);

        mvc.perform(get("/requests/1")
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("Нужна дрель")))
                .andExpect(jsonPath("$.created").exists());
    }
}
