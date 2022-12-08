package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.Create;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@StartBeforeEnd(groups = {Create.class})
public class BookingDto {
    private Long id;
    @NotNull(groups = {Create.class})
    private Long itemId;
    @NotNull(groups = {Create.class})
    @FutureOrPresent(groups = {Create.class})
    private LocalDateTime start;
    @NotNull(groups = {Create.class})
    @Future(groups = {Create.class})
    private LocalDateTime end;
}
