package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
public class User {
    private int user_id;
    private String user_name;
    private String user_email;
}
