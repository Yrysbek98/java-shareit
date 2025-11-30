package ru.practicum.shareit.request.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemResponseDto;


import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto {
    private Long id;
    private String description;
    private LocalDateTime created;
    private ItemResponseDto item;

}
