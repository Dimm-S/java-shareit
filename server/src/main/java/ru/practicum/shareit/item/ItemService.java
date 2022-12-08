package ru.practicum.shareit.item;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentInfoDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;

import java.util.List;

public interface ItemService {
    ItemDto saveItem(ItemDto itemDto, Long ownerId);

    CommentInfoDto saveComment(Long itemId, CommentDto commentDto, Long userId);

    ItemInfoDto getItemById(Long id, Long userId);

    List<ItemInfoDto> getAllItems(Long userId);

    ItemDto updateItem(Long id, ItemDto itemDto, Long ownerId);

    void deleteItem(Long id, Long ownerId);

    List<ItemDto> getItemsByQuery(String query);

    void checkItemExistence(Long id);

    void checkItemAvailable(BookingDto bookingDto);
}
