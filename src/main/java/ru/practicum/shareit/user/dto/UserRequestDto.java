package ru.practicum.shareit.user.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    @JsonProperty("name")
    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    @JsonProperty("email")
    @Email(message = "Некорректный email")
    @NotBlank(message = "Email не может быть пустым")
    private String email;
}
