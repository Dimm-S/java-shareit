package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapping itemMapping;
    private final UserRepository userRepository;

    @Override
    public ItemDto saveItem(ItemDto itemDto, long ownerId) {
        userRepository.checkUserExistence(ownerId);
        return itemMapping.mapToItemDto(
                itemRepository.saveItem(
                        itemMapping.mapToItem(itemDto, ownerId)));
    }

    @Override
    public ItemDto getItemById(long id) {
        itemRepository.checkItemExistence(id);
        return itemMapping.mapToItemDto(itemRepository.getItemById(id));
    }

    @Override
    public List<ItemDto> getAllItems(long userId) {
        return itemRepository.getAllItems(userId).stream()
                .map(itemMapping::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto updateItem(long id, ItemDto itemDto, long ownerId) {
        itemRepository.checkItemExistence(id);
        checkOwner(id, ownerId);
        return itemMapping.mapToItemDto(
                itemRepository.updateItem(
                        id, itemMapping.mapToItem(itemDto, ownerId)));
    }

    @Override
    public void deleteItem(long id, long ownerId) {
        itemRepository.checkItemExistence(id);
        checkOwner(id, ownerId);
        itemRepository.deleteItem(id);
    }

    @Override
    public List<ItemDto> getItemsByQuery(String query) {
        if (query.equals("")) {
            return new ArrayList<>();
        }
        return itemRepository.getItemsByQuery(query).stream()
                .map(itemMapping::mapToItemDto)
                .collect(Collectors.toList());
    }

    private void checkOwner(long itemId, long ownerId) {
        if (ownerId != itemRepository.getItemById(itemId).getOwner().getId()) {
            throw new NotFoundException("User with id=" + ownerId + " is not owner of item=" + itemId);
        }
    }
}
