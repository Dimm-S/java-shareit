package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryInMemory implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private long id = 0;

    @Override
    public Item saveItem(Item item) {
        item.setId(++id);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item getItemById(long id) {
        return items.get(id);
    }

    @Override
    public List<Item> getAllItems(long userId) {
        return new ArrayList<>(items.values())
                .stream()
                .filter(item -> item.getOwner().getId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public Item updateItem(long id, Item item) {
        if (item.getName() != null) {
            items.get(id).setName(item.getName());
        }
        if (item.getDescription() != null) {
            items.get(id).setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            items.get(id).setAvailable(item.getAvailable());
        }
        return items.get(id);
    }

    @Override
    public void deleteItem(long id) {
        items.remove(id);
    }

    @Override
    public void checkItemExistence(long id) {
        if (!items.containsKey(id)) {
            throw new NotFoundException("Item with id=" + id + " not found");
        }
    }

    @Override
    public List<Item> getItemsByQuery(String query) {
        return new ArrayList<>(items.values())
                .stream()
                .filter(item -> item.getName().toLowerCase().contains(query.toLowerCase())
                        || item.getDescription().toLowerCase().contains(query.toLowerCase()))
                .filter(item -> item.getAvailable().equals(true))
                .collect(Collectors.toList());
    }
}
