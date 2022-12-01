package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.CommentMapping;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentInfoDto;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
public class ItemServiceTest {
    private ItemServiceImpl itemService;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemMapping itemMapping;
    @Mock
    private UserService userService;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private CommentMapping commentMapping;

    final CommentInfoDto commentInfoDto = new CommentInfoDto(
            1L, "text", 1L, "name", null);
    final Item item = new Item(1L, "name", "desc", true, 1L, 1L);
    final ItemDto itemDto = new ItemDto(1L, "name", "desc", true, 1L);
    final ItemDto updItemDto = new ItemDto(1L, "updatedName", null, null, null);
    final ItemInfoDto itemInfoDto = new ItemInfoDto(1L, "name", "desc", 1L, true,
            null, new ItemInfoDto.BookingDto(1L,
            LocalDateTime.of(2023, 1, 1, 0, 0),
            LocalDateTime.of(2023, 2, 1, 0, 0), 1L),
            new ArrayList<>(Collections.singleton(commentInfoDto)));
    final Booking booking = new Booking(1L, LocalDateTime.of(2023, 1, 1, 0, 0),
            LocalDateTime.of(2023, 2, 1, 0, 0),
            1L,1L, Status.WAITING);
    final Comment comment = new Comment(1L, "text", 1L, 1L, null);
    final CommentDto commentDto = new CommentDto(1L, "text");
    final UserDto userDto = new UserDto(1L, "name", "name@email");

    @BeforeEach
    void setup() {
        itemService = new ItemServiceImpl(itemRepository, itemMapping, userService, bookingRepository,
                commentRepository, commentMapping);
    }

    @Test
    void saveItemTest() {
        when(itemRepository.save(any(Item.class)))
                .thenReturn(item);
        doCallRealMethod().when(itemMapping).mapToItem(any(ItemDto.class), anyLong());
        doCallRealMethod().when(itemMapping).mapToItemDto(any(Item.class));

        final ItemDto savedItem = itemService.saveItem(itemDto, 1L);

        assertEquals(itemDto, savedItem);
    }

    @Test
    void saveCommentTest() {
        when(bookingRepository.findAllByItemIdAndEndDate(anyLong(), any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>(Collections.singleton(booking)));
        when(commentRepository.save(any(Comment.class)))
                .thenReturn(comment);
        when(userService.getUserById(anyLong()))
                .thenReturn(userDto);
        doCallRealMethod().when(commentMapping).mapToComment(any(CommentDto.class), anyLong(), anyLong());
        doCallRealMethod().when(commentMapping).mapToCommentInfoDto(any(Comment.class), any(UserDto.class));

        final CommentInfoDto savedComment = itemService.saveComment(1L, commentDto, 1L);

        assertEquals(commentInfoDto, savedComment);
    }

    @Test
    void getItemByWrongIdTest() {
        when(itemRepository.existsById(anyLong()))
                .thenReturn(false);

        assertThrows(NotFoundException.class, () -> {
            itemService.checkItemExistence(1L);
            },"Item with id=1 not found");
    }

    @Test
    void getItemByIdTest() {
        when(itemRepository.existsById(anyLong()))
                .thenReturn(true);
        when(commentRepository.findAllByItemId(anyLong()))
                .thenReturn(new ArrayList<>(Collections.singleton(comment)));
        when(userService.getUserById(anyLong()))
                .thenReturn(userDto);
        when(bookingRepository.getAllBookingsByItemId(anyLong()))
                .thenReturn(new ArrayList<>(Collections.singleton(booking)));
        when(itemRepository.getReferenceById(anyLong()))
                .thenReturn(item);
        doCallRealMethod().when(commentMapping).mapToCommentInfoDto(any(Comment.class), any(UserDto.class));
        doCallRealMethod().when(itemMapping).mapToItemInfoDto(
                any(Item.class), any(), any(), anyList());

        final ItemInfoDto gettedItem = itemService.getItemById(1L, 1L);

        assertEquals(itemInfoDto, gettedItem);
    }

    @Test
    void getAllItemsTest() {
        when(itemRepository.getAllItemsByUserId(anyLong()))
                .thenReturn(new ArrayList<>(Collections.singleton(item)));
        when(itemRepository.existsById(anyLong()))
                .thenReturn(true);
        when(itemMapping.mapToItemInfoDto(any(), any(), any(), any()))
                .thenReturn(itemInfoDto);
        when(itemService.getItemById(1L, 1L))
                .thenReturn(itemInfoDto);

        final List<ItemInfoDto> itemInfoDtoList = itemService.getAllItems(1L);

        assertEquals(new ArrayList<>(Collections.singleton(itemInfoDto)), itemInfoDtoList);
    }

    @Test
    void updateItemTest() throws InstantiationException, IllegalAccessException {
        when(itemRepository.existsById(anyLong()))
                .thenReturn(true);
        when(itemRepository.getReferenceById(anyLong()))
                .thenReturn(item);
        when(itemRepository.save(any(Item.class)))
                .thenReturn(Item.class.newInstance());
        doCallRealMethod().when(itemMapping).mapToItemDto(any(Item.class));

        final ItemDto updatedItem = itemService.updateItem(1L, updItemDto, 1L);

        assertEquals("updatedName", updatedItem.getName());
    }

    @Test
    void deleteItemTest() {
        when(itemRepository.existsById(anyLong()))
                .thenReturn(true);
        when(itemRepository.getReferenceById(anyLong()))
                .thenReturn(item);
        doNothing().when(itemRepository).deleteById(any());
        itemService.deleteItem(1L, 1L);
    }

    @Test
    void getItemsByQuery() {
        when(itemRepository.findAll())
                .thenReturn(new ArrayList<>(Collections.singleton(item)));
        doCallRealMethod().when(itemMapping).mapToItemDto(any(Item.class));

        final List<ItemDto> result = itemService.getItemsByQuery("de");

        assertEquals(new ArrayList<>(Collections.singleton(itemDto)), result);

        final List<ItemDto> result2 = itemService.getItemsByQuery("");

        assertEquals(new ArrayList<>(), result2);
    }

    @Test
    void checkItemAvailableTest() {
        when(itemRepository.getReferenceById(anyLong()))
                .thenReturn(new Item(1L, "name", "desc", false, 1L, 1L));

        assertThrows(BadRequestException.class, () -> {
            itemService.checkItemAvailable(new BookingDto(1L, 1L,
                    LocalDateTime.of(2023, 1, 1, 0, 0),
                    LocalDateTime.of(2023, 2, 1, 0, 0)));
        },"Item 1 is unavailable");
    }
}
