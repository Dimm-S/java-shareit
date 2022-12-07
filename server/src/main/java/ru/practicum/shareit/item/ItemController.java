package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentInfoDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto addItem(@RequestBody ItemDto itemDto,
                           @RequestHeader(value = "X-Sharer-User-Id", required = true) Long ownerId) {
        log.info("Request endpoint: 'POST /items' (добавление новой вещи {}, владелец {})", itemDto, ownerId);
        return itemService.saveItem(itemDto, ownerId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentInfoDto addComment(@PathVariable Long itemId,
                                     @RequestBody CommentDto commentDto,
                                     @RequestHeader(value = "X-Sharer-User-Id", required = true) Long userId) {
        log.info("Request endpoint: 'POST /items/{}/comment' (add comment to item {} by user {})", itemId, itemId, userId);
        return itemService.saveComment(itemId, commentDto, userId);
    }


    @PatchMapping("/{id}")
    public ItemDto updateItem(@PathVariable Long id,
                              @RequestBody ItemDto itemDto,
                              @RequestHeader(value = "X-Sharer-User-Id", required = true) Long ownerId) {
        log.info("Request endpoint: 'PATCH /items/{}' (обновление вещи {}, владелец {})", id, itemDto, ownerId);
        return itemService.updateItem(id, itemDto, ownerId);
    }

    @GetMapping("/{id}")
    public ItemInfoDto getItem(@PathVariable Long id,
                               @RequestHeader(value = "X-Sharer-User-Id", required = true) Long userId) {
        log.info("Request endpoint: 'GET /items/{}' (Получение вещи по id)", id);
        return itemService.getItemById(id, userId);
    }

    @GetMapping
    public List<ItemInfoDto> getAllItemsByUserId(@RequestHeader(value = "X-Sharer-User-Id", required = true) Long userId) {
        log.info("Request endpoint: 'GET /items' (Получение списка всех вещей по id пользователя {})", userId);
        return itemService.getAllItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsByQuery(@RequestParam(value = "text", required = false) String query) {
        log.info("Request endpoint: 'GET /items/search?query={}'", query);
        return itemService.getItemsByQuery(query);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id,
                           @RequestHeader(value = "X-Sharer-User-Id", required = true) Long ownerId) {
        log.info("Request endpoint: 'DELETE /items/{}' (Удаление вещи по id, владелец {})", id, ownerId);
        itemService.deleteItem(id, ownerId);
    }
}
