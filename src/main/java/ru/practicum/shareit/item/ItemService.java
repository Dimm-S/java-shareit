package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto saveItem(ItemDto itemDto, long ownerId);

    ItemDto getItemById(long id);

    List<ItemDto> getAllItems(long userId);

    ItemDto updateItem(long id, ItemDto itemDto, long ownerId);

    void deleteItem(long id, long ownerId);

    List<ItemDto> getItemsByQuery(String query);
}
