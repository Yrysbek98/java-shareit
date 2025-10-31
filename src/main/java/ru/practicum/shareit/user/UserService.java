package ru.practicum.shareit.user;


import java.util.List;

public interface UserService {

    UserDto getUserById(int id);

    List<UserDto> getAllUsers();

    UserDto addNewUser(User user);

    UserDto updateUser(User user);

    void deleteUserById(int id);
}
