package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.Create;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class CommentDto {
    private Long id;
    @NotEmpty(groups = {Create.class})
    private String text;
}
