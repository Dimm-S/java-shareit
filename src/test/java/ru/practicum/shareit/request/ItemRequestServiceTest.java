package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInfoDto;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
public class ItemRequestServiceTest {
    private ItemRequestServiceImpl itemRequestService;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private ItemRequestMapping itemRequestMapping;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        itemRequestService = new ItemRequestServiceImpl(itemRequestRepository, itemRequestMapping, itemRepository, userService);
    }

    @Test
    void saveRequestTest() {
        ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "description", null);
        ItemRequest itemRequest = new ItemRequest(1L, "description", 2L, null);

        doCallRealMethod().when(itemRequestMapping).mapToItemRequest(any(ItemRequestDto.class), anyLong());
        when(itemRequestRepository.save(any(ItemRequest.class)))
                .thenReturn(itemRequest);
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);
        doCallRealMethod().when(itemRequestMapping).mapToItemRequestDto(any(ItemRequest.class));

        final ItemRequestDto savedItemRequest = itemRequestService.saveRequest(itemRequestDto, 2L);

        assertEquals(itemRequestDto, savedItemRequest);
    }

    @Test
    void getRequestByIdTest() {
        ItemRequestInfoDto infoDto = new ItemRequestInfoDto(1L,
                "description",
                null,
                new ArrayList<>(Collections.singleton(new ItemRequestInfoDto.ItemDto(
                        1L, "name", "description", true, 1L, 1L))));

        when(itemRepository.getAllItemsByRequestId(anyLong()))
                .thenReturn(new ArrayList<>(Collections.singleton(new Item(
                        1L, "name", "description", true, 1L, 1L))));
        when(itemRequestRepository.existsById(anyLong()))
                .thenReturn(true);

        when(itemRequestRepository.getReferenceById(anyLong()))
                .thenReturn(new ItemRequest(1L, "description", 1L, null));
        doCallRealMethod().when(itemRequestMapping).mapToInfoDto(any(ItemRequest.class), anyList());

        final ItemRequestInfoDto itemRequestInfoDto = itemRequestService.getRequestById(1L, 1L);

        assertEquals(infoDto, itemRequestInfoDto);
    }

    @Test
    void getAllRequestsByUserTest() {
        List<ItemRequestInfoDto> itemRequestInfoDtoList = new ArrayList<>(Collections.singleton(new ItemRequestInfoDto(1L,
                "description",
                null,
                new ArrayList<>(Collections.singleton(new ItemRequestInfoDto.ItemDto(
                        1L, "name", "description", true, 1L, 1L))))));

        when(itemRequestRepository.findAllByRequesterId(anyLong()))
                .thenReturn(new ArrayList<>(Collections.singleton(new ItemRequest(
                        1L, "description", 1L, null))));
        when(userRepository.existsById(any()))
                .thenReturn(true);
        when(itemRequestRepository.existsById(any()))
                .thenReturn(true);
        when(itemRepository.getAllItemsByRequestId(anyLong()))
                .thenReturn(new ArrayList<>(Collections.singleton(new Item(
                        1L, "name", "description", true, 1L, 1L))));
        when(itemRequestRepository.getReferenceById(anyLong()))
                .thenReturn(new ItemRequest(1L, "description", 1L, null));
        doCallRealMethod().when(itemRequestMapping).mapToInfoDto(any(), any());

        final List<ItemRequestInfoDto> list = itemRequestService.getAllRequestsByUser(1L);

        assertEquals(itemRequestInfoDtoList, list);
    }

    @Test
    void getAllOtherUsersRequestsTest() {
        List<ItemRequestInfoDto> itemRequestInfoDtoList = new ArrayList<>(Collections.singleton(new ItemRequestInfoDto(1L,
                "description",
                null,
                new ArrayList<>(Collections.singleton(new ItemRequestInfoDto.ItemDto(
                        1L, "name", "description", true, 1L, 1L))))));

        when(itemRequestRepository.existsById(anyLong()))
                .thenReturn(true);
        when(itemRequestRepository.getAllOtherUsersRequests(any(PageRequest.class), anyLong()))
                .thenReturn(new ArrayList<>(Collections.singleton(new ItemRequest(
                        1L, "description", 1L, null))));
        when(itemRepository.getAllItemsByRequestId(anyLong()))
                .thenReturn(new ArrayList<>(Collections.singleton(
                        new Item(1L, "name", "description", true, 1L, 1L))));
        when(itemRequestRepository.getReferenceById(anyLong()))
                .thenReturn(new ItemRequest(1L, "description", 2L, null));
        doCallRealMethod().when(itemRequestMapping).mapToInfoDto(any(ItemRequest.class), anyList());

        final List<ItemRequestInfoDto> list = itemRequestService.getAllOtherUsersRequests(
                0, 10, 1L);

        assertEquals(itemRequestInfoDtoList, list);
    }
}
