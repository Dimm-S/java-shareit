package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/items")
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> addItem(@Validated({Create.class}) @RequestBody ItemDto itemDto,
                                          @RequestHeader(value = "X-Sharer-User-Id", required = true) Long ownerId) {
        log.info("Request endpoint: 'POST /items' (добавление новой вещи {}, владелец {})", itemDto, ownerId);
        return itemClient.saveItem(itemDto, ownerId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@PathVariable Long itemId,
                                     @Validated({Create.class}) @RequestBody CommentDto commentDto,
                                     @RequestHeader(value = "X-Sharer-User-Id", required = true) Long userId) {
        log.info("Request endpoint: 'POST /items/{}/comment' (add comment to item {} by user {})", itemId, itemId, userId);
        return itemClient.saveComment(itemId, commentDto, userId);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateItem(@PathVariable Long id,
                              @Validated({Update.class}) @RequestBody ItemDto itemDto,
                              @RequestHeader(value = "X-Sharer-User-Id", required = true) Long ownerId) {
        log.info("Request endpoint: 'PATCH /items/{}' (обновление вещи {}, владелец {})", id, itemDto, ownerId);
        return itemClient.updateItem(id, itemDto, ownerId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItem(@PathVariable Long id,
                               @RequestHeader(value = "X-Sharer-User-Id", required = true) Long userId) {
        log.info("Request endpoint: 'GET /items/{}' (Получение вещи по id)", id);
        return itemClient.getItemById(id, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemsByUserId(@RequestHeader(value = "X-Sharer-User-Id", required = true) Long userId) {
        log.info("Request endpoint: 'GET /items' (Получение списка всех вещей по id пользователя {})", userId);
        return itemClient.getAllItems(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItemsByQuery(@RequestParam(value = "text", required = false) String query) {
        log.info("Request endpoint: 'GET /items/search?query={}'", query);
        return itemClient.getItemsByQuery(query);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id,
                           @RequestHeader(value = "X-Sharer-User-Id", required = true) Long ownerId) {
        log.info("Request endpoint: 'DELETE /items/{}' (Удаление вещи по id, владелец {})", id, ownerId);
        itemClient.deleteItem(id, ownerId);
    }

}
