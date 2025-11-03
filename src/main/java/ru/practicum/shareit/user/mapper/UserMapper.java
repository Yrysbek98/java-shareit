package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public class UserMapper {
    public static User toEntity(UserDto dto) {
        if (dto == null) return null;

        User user = new User();
        user.setUserId(dto.getId());
        user.setUserName(dto.getName());
        user.setUserEmail(dto.getEmail());
        return user;
    }

    public static UserDto toDto(User user) {
        if (user == null) return null;
        UserDto dto = new UserDto();
        dto.setId(user.getUserId());
        dto.setName(user.getUserName());
        dto.setEmail(user.getUserEmail());
        return dto;
    }

}
