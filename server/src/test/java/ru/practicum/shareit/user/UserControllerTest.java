package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.contoller.UserController;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    @Test
    void createUser_shouldReturnCreatedUser() throws Exception {
        UserResponseDto inputDto = new UserResponseDto(null, "John Doe", "john@example.com");
        UserResponseDto outputDto = new UserResponseDto(1L, "John Doe", "john@example.com");

        when(userService.addNewUser(any(UserRequestDto.class))).thenReturn(outputDto);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(inputDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("John Doe")))
                .andExpect(jsonPath("$.email", is("john@example.com")));
    }

    @Test
    void getAllUsers_shouldReturnListOfUsers() throws Exception {
        UserResponseDto user1 = new UserResponseDto(1L, "User 1", "user1@example.com");
        UserResponseDto user2 = new UserResponseDto(2L, "User 2", "user2@example.com");

        when(userService.getAllUsers()).thenReturn(List.of(user1, user2));

        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("User 1")))
                .andExpect(jsonPath("$[1].name", is("User 2")));
    }

    @Test
    void getUserById_shouldReturnUser() throws Exception {
        UserResponseDto userDto = new UserResponseDto(1L, "John Doe", "john@example.com");

        when(userService.getUserById(1L)).thenReturn(userDto);

        mvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("John Doe")))
                .andExpect(jsonPath("$.email", is("john@example.com")));
    }

    @Test
    void updateUser_shouldReturnUpdatedUser() throws Exception {
        UserResponseDto updateDto = new UserResponseDto(null, "Updated Name", null);
        UserResponseDto outputDto = new UserResponseDto(1L, "Updated Name", "john@example.com");

        when(userService.updateUser(eq(1L), any(UserRequestDto.class))).thenReturn(outputDto);

        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(updateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Updated Name")));
    }

    @Test
    void deleteUser_shouldReturnNoContent() throws Exception {
        doNothing().when(userService).deleteUserById(1L);

        mvc.perform(delete("/users/1"))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUserById(1L);
    }
}
