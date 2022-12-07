package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfoDto;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto addRequest(@RequestBody ItemRequestDto itemRequestDto,
                                     @RequestHeader(value = "X-Sharer-User-Id", required = true) Long userId) {
        log.info("Request endpoint 'POST /requests' добавление нового запроса {} пользователем {}",
                itemRequestDto, userId);
        return itemRequestService.saveRequest(itemRequestDto, userId);
    }

    @GetMapping
    public List<ItemRequestInfoDto> getAllRequestsByUser(
            @RequestHeader(value = "X-Sharer-User-Id", required = true) Long userId) {
        log.info("Request endpoint 'GET /requests' получение всех запросов пользователя {}", userId);
        return itemRequestService.getAllRequestsByUser(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestInfoDto getRequestById(@PathVariable Long requestId,
                                             @RequestHeader(value = "X-Sharer-User-Id", required = true) Long userId) {
        log.info("Request endpoint 'GET /requests/{}' получение запроса пользователем {}", requestId, userId);
        return itemRequestService.getRequestById(requestId, userId);
    }

    @GetMapping("/all")
    public List<ItemRequestInfoDto> getAllOtherUsersRequests(
            @RequestHeader(value = "X-Sharer-User-Id", required = true) Long userId,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Request endpoint 'GET /requests/all' получение всех запросов");
        return itemRequestService.getAllOtherUsersRequests(from, size, userId);
    }
}
