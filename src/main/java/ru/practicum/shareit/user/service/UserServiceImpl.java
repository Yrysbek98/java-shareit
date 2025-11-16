package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.exceptionType.ConflictException;
import ru.practicum.shareit.exception.exceptionType.NotFoundException;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserResponseDto getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("Вещь с id = " + id + " не найдена");
        }
        return UserMapper.toDto(user.get());
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDto)
                .toList();
    }

    @Override
    public UserResponseDto addNewUser(UserRequestDto userDto) {
        if (userRepository.existsByUserEmail(userDto.getEmail())) {
            throw new ConflictException("Email уже используется");
        }
        User user = UserMapper.toEntity(userDto);
        User saved = userRepository.save(user);
        return UserMapper.toDto(saved);
    }

    @Override
    public UserResponseDto updateUser(Long id, UserRequestDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден"));

        if (userDto.getEmail() != null && !userDto.getEmail().equals(user.getUserEmail())) {
            if (userRepository.existsByUserEmail(userDto.getEmail())) {
                throw new ConflictException("Email уже используется");
            }
            user.setUserEmail(userDto.getEmail());
        }

        if (userDto.getName() != null) {
            user.setUserName(userDto.getName());
        }

        User updated = userRepository.save(user);
        return UserMapper.toDto(updated);
    }

    @Override
    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
        userRepository.deleteById(id);
    }
}

