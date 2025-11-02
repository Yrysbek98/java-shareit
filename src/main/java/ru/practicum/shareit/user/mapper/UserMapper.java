package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public class UserMapper {
    public static User toEntity(UserDto dto) {
        User user = new User();
        user.setUser_name(dto.getUser_name());
        user.setUser_email(dto.getUser_email());
        return user;
    }

    public static UserDto toDto(User user) {
        return new UserDto(user.getUser_id(), user.getUser_name(), user.getUser_email());
    }

}
