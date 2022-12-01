package ru.practicum.shareit.request;

import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfoDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto saveRequest(ItemRequestDto itemRequestDto, Long userId);

    List<ItemRequestInfoDto> getAllRequestsByUser(Long userId);

    ItemRequestInfoDto getRequestById(Long requestId);

    List<ItemRequestInfoDto> getAllOtherUsersRequests(PageRequest pageRequest, Long userId);
}
