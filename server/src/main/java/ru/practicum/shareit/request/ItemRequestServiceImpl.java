package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfoDto;
import ru.practicum.shareit.user.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRequestMapping itemRequestMapping;
    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    public ItemRequestDto saveRequest(ItemRequestDto itemRequestDto, Long userId) {
        userService.checkUserExistence(userId);
        return itemRequestMapping.mapToItemRequestDto(
                itemRequestRepository.save(
                        itemRequestMapping.mapToItemRequest(itemRequestDto, userId)));
    }

    @Override
    public List<ItemRequestInfoDto> getAllRequestsByUser(Long userId) {
        userService.checkUserExistence(userId);
        List<ItemRequest> requests = itemRequestRepository.findAllByRequesterId(userId);
        return requests.stream()
                .map(request -> this.getRequestById(request.getId(), userId))
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestInfoDto getRequestById(Long requestId, Long userId) {
        checkRequestExistence(requestId);
        userService.checkUserExistence(userId);
        List<Item> items = itemRepository.getAllItemsByRequestId(requestId);
        ItemRequest request = itemRequestRepository.getReferenceById(requestId);
        return itemRequestMapping.mapToInfoDto(request, items);
    }

    @Override
    public List<ItemRequestInfoDto> getAllOtherUsersRequests(Integer from, Integer size, Long userId) {
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size);
        List<ItemRequest> itemRequests = itemRequestRepository.getAllOtherUsersRequests(pageRequest, userId);
        return itemRequests.stream()
                .map(ItemRequest::getId)
                .map(id -> getRequestById(id, userId))
                .collect(Collectors.toList());
    }

    public void checkRequestExistence(Long requestId) {
        if (!itemRequestRepository.existsById(requestId)) {
            throw new NotFoundException("Request not found");
        }
    }
}
