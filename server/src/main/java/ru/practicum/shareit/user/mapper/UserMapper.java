package ru.practicum.shareit.user.mapper;


import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;

public class UserMapper {
    public static User toEntity(UserRequestDto dto) {
        if (dto == null) return null;
        return new User(dto.getName(), dto.getEmail());
    }

    public static UserResponseDto toDto(User user) {
        if (user == null) return null;
        return new UserResponseDto(user.getId(), user.getName(), user.getEmail());
    }

}
