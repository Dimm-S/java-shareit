package ru.practicum.shareit.user.dto;

import lombok.Data;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class UserDto {
    private Long id;
    @NotNull(groups = {Create.class})
    private String name;
    @NotNull(groups = {Create.class})
    @Pattern(regexp = "^.*@.*$", groups = {Create.class})
    @Pattern(regexp = "^.*@.*$", groups = {Update.class})
    private String email;
}
