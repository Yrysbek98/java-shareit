package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private HashMap<Long, User> users = new HashMap<>();
    private Long nextId = 1L;

    @Override
    public UserDto getUserById(Long id) {
        return UserMapper.toDto(users.get(id));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return users.values().stream()
                .map(UserMapper::toDto)
                .toList();
    }

    @Override
    public UserDto addNewUser(UserDto userDto) {
        User user = UserMapper.toEntity(userDto);
        user.setUser_id(nextId++);
        users.put(user.getUser_id(), user);
        return UserMapper.toDto(user);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User user = UserMapper.toEntity(userDto);
        users.put(user.getUser_id(), user);
        return UserMapper.toDto(user);
    }

    @Override
    public void deleteUserById(Long id) {
        users.remove(id);
    }
}
