package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto addItem(@Validated({Create.class}) @RequestBody ItemDto itemDto,
                           @RequestHeader(value = "X-Sharer-User-Id", required = true) long ownerId) {
        log.info("Request endpoint: 'POST /items' (добавление новой вещи {}, владелец {})", itemDto, ownerId);
        return itemService.saveItem(itemDto, ownerId);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@PathVariable long id,
                              @Validated({Update.class}) @RequestBody ItemDto itemDto,
                              @RequestHeader(value = "X-Sharer-User-Id", required = true) long ownerId) {
        log.info("Request endpoint: 'PATCH /items/{}' (обновление вещи {}, владелец {})", id, itemDto, ownerId);
        return itemService.updateItem(id, itemDto, ownerId);
    }

    @GetMapping("/{id}")
    public ItemDto getItem(@PathVariable long id) {
        log.info("Request endpoint: 'GET /items/{}' (Получение вещи по id)", id);
        return itemService.getItemById(id);
    }

    @GetMapping
    public List<ItemDto> getAllItemsByUserId(@RequestHeader(value = "X-Sharer-User-Id", required = true) long userId) {
        log.info("Request endpoint: 'GET /items' (Получение списка всех вещей по id пользователя {})", userId);
        return itemService.getAllItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsByQuery(@RequestParam(value = "text", required = false) String query) {
        log.info("Request endpoint: 'GET /items/search?query={}'", query);
        return itemService.getItemsByQuery(query);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable long id,
                           @RequestHeader(value = "X-Sharer-User-Id", required = true) long ownerId) {
        log.info("Request endpoint: 'DELETE /items/{}' (Удаление вещи по id, владелец {})", id, ownerId);
        itemService.deleteItem(id, ownerId);
    }
}
