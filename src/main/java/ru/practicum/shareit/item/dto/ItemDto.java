package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
public class ItemDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long itemId;

    @NotBlank(message = "Название не должно быть пустым")
    private String itemName;

    @NotBlank(message = "Описание не должно быть пустым")
    private String itemDescription;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long ownerId;
}
