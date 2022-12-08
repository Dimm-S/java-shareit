package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Validated
@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> addRequest(@Validated({Create.class}) @RequestBody ItemRequestDto itemRequestDto,
                                             @RequestHeader(value = "X-Sharer-User-Id", required = true) Long userId) {
        log.info("Request endpoint 'POST /requests' добавление нового запроса {} пользователем {}",
                itemRequestDto, userId);
        return itemRequestClient.saveRequest(itemRequestDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllRequestsByUser(
            @RequestHeader(value = "X-Sharer-User-Id", required = true) Long userId) {
        log.info("Request endpoint 'GET /requests' получение всех запросов пользователя {}", userId);
        return itemRequestClient.getAllRequestsByUser(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@PathVariable Long requestId,
                                             @RequestHeader(value = "X-Sharer-User-Id", required = true) Long userId) {
        log.info("Request endpoint 'GET /requests/{}' получение запроса пользователем {}", requestId, userId);
        return itemRequestClient.getRequestById(requestId, userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllOtherUsersRequests(
            @RequestHeader(value = "X-Sharer-User-Id", required = true) Long userId,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Request endpoint 'GET /requests/all' получение всех запросов");
        return itemRequestClient.getAllOtherUsersRequests(from, size, userId);
    }
}
