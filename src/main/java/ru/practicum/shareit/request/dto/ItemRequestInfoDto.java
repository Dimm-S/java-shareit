package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ItemRequestInfoDto {
    private Long id;
    private String description;
    private LocalDateTime created;
    private List<ItemDto> items;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ItemDto {
        Long id;
        String name;
        String description;
        Boolean available;
        Long ownerId;
        Long requestId;
    }
}
