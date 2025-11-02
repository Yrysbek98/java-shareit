package ru.practicum.shareit.user;


import java.util.List;

public interface UserService {

    UserDto getUserById(int id);

    List<UserDto> getAllUsers();

    UserDto addNewUser(UserDto userDto);

    UserDto updateUser(UserDto userDto);

    void deleteUserById(int id);
}
