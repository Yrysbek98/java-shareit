package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceIntegrationTest {

    private final UserService userService;

    @Test
    void createUser_shouldCreateAndReturnUser() {
        UserRequestDto userDto = new UserRequestDto("John Doe", "john@example.com");

        UserResponseDto createdUser = userService.addNewUser(userDto);

        assertThat(createdUser.getId(), notNullValue());
        assertThat(createdUser.getName(), equalTo("John Doe"));
        assertThat(createdUser.getEmail(), equalTo("john@example.com"));
    }

    @Test
    void getAllUsers_shouldReturnAllUsers() {
        UserRequestDto user1 = new UserRequestDto("User 1", "user1@example.com");
        UserRequestDto user2 = new UserRequestDto("User 2", "user2@example.com");

        userService.addNewUser(user1);
        userService.addNewUser(user2);

        List<UserResponseDto> users = userService.getAllUsers();

        assertThat(users, hasSize(greaterThanOrEqualTo(2)));
        assertThat(users.stream().map(UserResponseDto::getEmail).toList(),
                hasItems("user1@example.com", "user2@example.com"));
    }

    @Test
    void getUserById_shouldReturnUser() {
        UserRequestDto userDto = new UserRequestDto("John Doe", "john@example.com");
        UserResponseDto createdUser = userService.addNewUser(userDto);

        UserResponseDto foundUser = userService.getUserById(createdUser.getId());

        assertThat(foundUser.getId(), equalTo(createdUser.getId()));
        assertThat(foundUser.getName(), equalTo("John Doe"));
        assertThat(foundUser.getEmail(), equalTo("john@example.com"));
    }

    @Test
    void updateUser_shouldUpdateUserName() {
        UserRequestDto userDto = new UserRequestDto("John Doe", "john@example.com");
        UserResponseDto createdUser = userService.addNewUser(userDto);

        UserRequestDto updateDto = new UserRequestDto("Updated Name", null);

        UserResponseDto updatedUser = userService.updateUser(createdUser.getId(), updateDto);

        assertThat(updatedUser.getId(), equalTo(createdUser.getId()));
        assertThat(updatedUser.getName(), equalTo("Updated Name"));
        assertThat(updatedUser.getEmail(), equalTo("john@example.com")); // Email не изменился
    }

    @Test
    void updateUser_shouldUpdateUserEmail() {
        UserRequestDto userDto = new UserRequestDto("John Doe", "john@example.com");
        UserResponseDto createdUser = userService.addNewUser(userDto);

        UserRequestDto updateDto = new UserRequestDto(null, "newemail@example.com");

        UserResponseDto updatedUser = userService.updateUser(createdUser.getId(), updateDto);


        assertThat(updatedUser.getId(), equalTo(createdUser.getId()));
        assertThat(updatedUser.getName(), equalTo("John Doe")); // Имя не изменилось
        assertThat(updatedUser.getEmail(), equalTo("newemail@example.com"));
    }

    @Test
    void deleteUser_shouldRemoveUser() {
        UserRequestDto userDto = new UserRequestDto("John Doe", "john@example.com");
        UserResponseDto createdUser = userService.addNewUser(userDto);

        userService.deleteUserById(createdUser.getId());

        List<UserResponseDto> users = userService.getAllUsers();
        assertThat(users.stream()
                .filter(u -> u.getId().equals(createdUser.getId()))
                .findAny()
                .isEmpty(), is(true));
    }
}
