package ru.practicum.shareit.user.service;


import jakarta.transaction.Transactional;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;


import java.util.List;


public interface UserService {
    UserResponseDto getUserById(Long id);

    List<UserResponseDto> getAllUsers();

    @Transactional
    UserResponseDto addNewUser(UserRequestDto userDto);

    @Transactional
    UserResponseDto updateUser(Long id, UserRequestDto userDto);

    @Transactional
    void deleteUserById(Long id);
}
