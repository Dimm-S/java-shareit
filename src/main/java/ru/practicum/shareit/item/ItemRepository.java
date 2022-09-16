package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item saveItem(Item item);

    Item getItemById(long id);

    List<Item> getAllItems(long userId);

    Item updateItem(long id, Item item);

    void deleteItem(long id);

    void checkItemExistence(long id);

    List<Item> getItemsByQuery(String query);
}
