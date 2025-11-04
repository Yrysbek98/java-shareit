package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserConflictException;
import ru.practicum.shareit.user.exception.UserValidationException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final ConcurrentHashMap<Long, User> users = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    @Override
    public UserDto getUserById(Long id) {
        User user = users.get(id);
        return user == null ? null : UserMapper.toDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return users.values().stream()
                .map(UserMapper::toDto)
                .toList();
    }

    @Override
    public UserDto addNewUser(UserDto userDto) {
        if (userDto == null) {
            throw new UserValidationException("Данные пользователя не могут быть пустыми");
        }

        if (userDto.getEmail() != null &&
                users.values().stream().anyMatch(u -> u.getUserEmail() != null &&
                        u.getUserEmail().equalsIgnoreCase(userDto.getEmail()))) {
            throw new UserConflictException("Такой email уже существует");
        }

        User user = UserMapper.toEntity(userDto);
        long id = idGenerator.getAndIncrement();
        user.setUserId(id);
        users.put(id, user);
        return UserMapper.toDto(user);
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = users.get(id);
        if (user == null) {
            throw new UserValidationException("Пользователь с id " + id + " не найден");
        }
        if (userDto.getEmail() != null &&
                users.values().stream().anyMatch(u -> u.getUserEmail() != null &&
                        u.getUserEmail().equalsIgnoreCase(userDto.getEmail()))) {
            throw new UserConflictException("Такой email уже существует");
        }

        if (userDto.getName() != null) {
            user.setUserName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            user.setUserEmail(userDto.getEmail());
        }


        users.put(id, user);
        return UserMapper.toDto(user);
    }

    @Override
    public void deleteUserById(Long id) {
        users.remove(id);
    }
}