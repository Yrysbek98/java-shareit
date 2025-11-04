package ru.practicum.shareit.user.model;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private Long userId;
    private String userName;
    private String userEmail;
}
