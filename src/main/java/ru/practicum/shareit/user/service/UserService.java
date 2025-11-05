package ru.practicum.shareit.user.service;


import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

import java.util.List;

public interface UserService {

    UserResponseDto getUserById(Long id);

    List<UserResponseDto> getAllUsers();

    UserResponseDto addNewUser(UserRequestDto userDto);

    UserResponseDto updateUser(Long id, UserRequestDto userDto);

    void deleteUserById(Long id);
}
