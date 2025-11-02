package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private HashMap<Integer, User> users = new HashMap<>();
    private int nextId = 1;

    @Override
    public UserDto getUserById(int id) {
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
    public void deleteUserById(int id) {
        users.remove(id);
    }
}
