package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfoDto;
import ru.practicum.shareit.user.UserService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    private final UserService userService;

    @PostMapping
    public ItemRequestDto addRequest(@Validated({Create.class}) @RequestBody ItemRequestDto itemRequestDto,
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
        userService.checkUserExistence(userId);
        return itemRequestService.getRequestById(requestId);
    }

    @GetMapping("/all")
    public List<ItemRequestInfoDto> getAllOtherUsersRequests(
            @RequestHeader(value = "X-Sharer-User-Id", required = true) Long userId,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Request endpoint 'GET /requests/all' получение всех запросов");
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size);
        return itemRequestService.getAllOtherUsersRequests(pageRequest, userId);
    }
}
