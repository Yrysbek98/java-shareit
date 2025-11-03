package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public class UserMapper {
    public static User toEntity(UserDto dto) {
        if (dto == null) return null;

        User user = new User();
        user.setUserId(dto.getUserId());
        user.setUserName(dto.getUserName());
        user.setUserEmail(dto.getUserEmail());
        return user;
    }

    public static UserDto toDto(User user) {
        if (user == null) return null;
        return new UserDto(user.getUserId(), user.getUserName(), user.getUserEmail());
    }

}
