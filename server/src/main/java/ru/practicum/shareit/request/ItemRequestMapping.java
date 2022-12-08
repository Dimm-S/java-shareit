package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfoDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestMapping {

    public ItemRequest mapToItemRequest(ItemRequestDto itemRequestDto, Long userId) {
        return new ItemRequest(
                itemRequestDto.getId(),
                itemRequestDto.getDescription(),
                userId,
                LocalDateTime.now()
        );
    }

    public ItemRequestDto mapToItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated()
        );
    }

    public ItemRequestInfoDto mapToInfoDto(ItemRequest itemRequest, List<Item> items) {
        List<ItemRequestInfoDto.ItemDto> itemsDto = new ArrayList<>();
        for (Item item : items) {
            ItemRequestInfoDto.ItemDto itemDto = mapItemForItemRequestInfoDto(item);
            itemsDto.add(itemDto);
        }
        return new ItemRequestInfoDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated(),
                itemsDto);
    }

    private static ItemRequestInfoDto.ItemDto mapItemForItemRequestInfoDto(Item item) {
        return new ItemRequestInfoDto.ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwnerId(),
                item.getRequestId());
    }
}
