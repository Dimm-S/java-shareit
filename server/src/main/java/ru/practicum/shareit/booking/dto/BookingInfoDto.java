package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.Status;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingInfoDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Status status;
    private Long bookerId;
    private Long itemId;
    private String itemName;
    private BookerDto booker;
    private ItemDto item;

    @Data
    @AllArgsConstructor
    public static class BookerDto {
        Long id;
        String name;
    }

    @Data
    @AllArgsConstructor
    public static class ItemDto {
        Long id;
        String name;
        Long ownerId;
    }
}
