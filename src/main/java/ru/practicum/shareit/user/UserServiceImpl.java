package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements  UserService{
    @Override
    public UserDto getUserById(int id) {
        return null;
    }

    @Override
    public List<UserDto> getAllUsers() {
        return List.of();
    }

    @Override
    public UserDto addNewUser(User user) {
        return null;
    }

    @Override
    public UserDto updateUser(User user) {
        return null;
    }

    @Override
    public void deleteUserById(int id) {

    }
}
