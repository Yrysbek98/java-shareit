package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.exceptionType.ConflictException;
import ru.practicum.shareit.exception.exceptionType.ValidationException;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
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
    public UserResponseDto getUserById(Long id) {
        User user = users.get(id);
        return UserMapper.toDto(user);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return users.values().stream()
                .map(UserMapper::toDto)
                .toList();
    }

    @Override
    public UserResponseDto addNewUser(UserRequestDto userDto) {
        if (userDto == null) {
            throw new ValidationException("Данные пользователя не могут быть пустыми");
        }

        if (userDto.getEmail() != null &&
                users.values().stream().anyMatch(u -> u.getUserEmail() != null &&
                        u.getUserEmail().equalsIgnoreCase(userDto.getEmail()))) {
            throw new ConflictException("Такой email уже существует");
        }

        User user = UserMapper.toEntity(userDto);
        long id = idGenerator.getAndIncrement();
        user.setUserId(id);
        users.put(id, user);
        return UserMapper.toDto(user);
    }

    @Override
    public UserResponseDto updateUser(Long id, UserRequestDto userDto) {
        User user = users.get(id);
        if (user == null) {
            throw new ValidationException("Пользователь с id " + id + " не найден");
        }
        if (userDto.getEmail() != null &&
                users.values().stream()
                        .anyMatch(u -> u.getUserEmail() != null &&
                                u.getUserEmail().equalsIgnoreCase(userDto.getEmail()) &&
                                !u.getUserId().equals(id))) {
            throw new ConflictException("Такой email уже существует");
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