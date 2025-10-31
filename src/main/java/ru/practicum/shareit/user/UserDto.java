package ru.practicum.shareit.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int user_id;
    @NotBlank(message = "Имя не может быть пустым")
    private String user_name;
    @Email(message = "Некорретный email")
    @NotBlank(message = "Email - неможет быть пустым")
    private String user_email;
}
