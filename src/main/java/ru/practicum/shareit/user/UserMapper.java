package ru.practicum.shareit.user;

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
