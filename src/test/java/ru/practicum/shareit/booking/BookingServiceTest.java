package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
public class BookingServiceTest {
    private BookingServiceImpl bookingService;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private BookingMapping bookingMapping;
    @Mock
    private UserService userService;
    @Mock
    private ItemService itemService;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;

    final Booking booking = new Booking(1L,
            LocalDateTime.of(2023, 1, 1, 0, 0),
            LocalDateTime.of(2023, 2, 1, 0, 0),
            1L, 1L, Status.WAITING
    );
    final Booking bookingCurrent = new Booking(2L,
            LocalDateTime.of(2022, 1, 1, 0, 0),
            LocalDateTime.of(2023, 2, 1, 0, 0),
            1L, 1L, Status.CURRENT
    );
    final Booking bookingPast = new Booking(2L,
            LocalDateTime.of(2022, 1, 1, 0, 0),
            LocalDateTime.of(2022, 2, 1, 0, 0),
            1L, 1L, Status.CANCELED
    );
    final BookingDto bookingDto = new BookingDto(1L, 1L,
            LocalDateTime.of(2023, 1, 1, 0, 0),
            LocalDateTime.of(2023, 2, 1, 0, 0)
    );
    final BookingInfoDto bookingInfoDto = new BookingInfoDto(1L,
            LocalDateTime.of(2023, 1, 1, 0, 0),
            LocalDateTime.of(2023, 2, 1, 0, 0),
            Status.WAITING, 1L, 1L, "item",
            new BookingInfoDto.BookerDto(1L, "name"),
            new BookingInfoDto.ItemDto(1L, "item", 1L)
    );
    final BookingInfoDto bookingInfoDtoPast = new BookingInfoDto(2L,
            LocalDateTime.of(2022, 1, 1, 0, 0),
            LocalDateTime.of(2022, 2, 1, 0, 0),
            Status.CANCELED, 1L, 1L, "item",
            new BookingInfoDto.BookerDto(1L, "name"),
            new BookingInfoDto.ItemDto(1L, "item", 1L)
    );
    final ItemInfoDto itemInfoDto = new ItemInfoDto(1L, "item", "description", 2L, true,
            new ItemInfoDto.BookingDto(), new ItemInfoDto.BookingDto(), new ArrayList<>()
    );
    final User user = new User(1L, "name", "name@email");
    final Item item = new Item(1L, "item", "description", true, 1L, 1L);

    @BeforeEach
    void setup() {
        bookingService = new BookingServiceImpl(bookingRepository, userService, new BookingMapping(itemRepository, userRepository), itemService);
    }

    @Test
    void saveBookingTest() {
        when(itemService.getItemById(anyLong(), anyLong()))
                .thenReturn(itemInfoDto);
        when(itemRepository.getReferenceById(any()))
                .thenReturn(item);
        when(userRepository.getReferenceById(any()))
                .thenReturn(user);
        when(bookingRepository.save(any(Booking.class)))
                .thenReturn(booking);

        final BookingInfoDto savedBooking = bookingService.saveBooking(bookingDto, 1L);

        assertEquals(bookingInfoDto, savedBooking);
    }

    @Test
    void getBookingById() {
        when(bookingRepository.existsById(anyLong()))
                .thenReturn(true);
        when(bookingRepository.getReferenceById(anyLong()))
                .thenReturn(booking);
        when(itemRepository.getReferenceById(any()))
                .thenReturn(item);
        when(userRepository.getReferenceById(any()))
                .thenReturn(user);

        final BookingInfoDto bookingInfoDto1 = bookingService.getBookingById(1L, 1L);

        assertEquals(bookingInfoDto, bookingInfoDto1);
    }

    @Test
    void approveBookingByIdTest() {
        when(bookingRepository.getReferenceById(anyLong()))
                .thenReturn(booking);
        when(itemRepository.getReferenceById(any()))
                .thenReturn(item);
        when(userRepository.getReferenceById(any()))
                .thenReturn(user);

        final BookingInfoDto bookingInfoDto1 = bookingService.approveBookingById(1L, 1L, true);

        assertEquals(Status.APPROVED, bookingInfoDto1.getStatus());
    }

    @Test
    void getAllBookingsByUserIdTest() {
        when(bookingRepository.findAllBookingsByBookerId(anyLong(), any(PageRequest.class)))
                .thenReturn(new ArrayList<>(Collections.singleton(booking)));
        when(itemRepository.getReferenceById(any()))
                .thenReturn(item);
        when(userRepository.getReferenceById(any()))
                .thenReturn(user);

        final List<BookingInfoDto> bookingInfoDtoList = bookingService.getAllBookingsByUserId(
                1L, "WAITING", PageRequest.ofSize(10));

        assertEquals(new ArrayList<>(Collections.singleton(bookingInfoDto)), bookingInfoDtoList);
    }

    @Test
    void getAllBookingsByOwnerIdTest() {
        when(bookingRepository.getAllBookingsByOwnerId(anyLong(), any(PageRequest.class)))
                .thenReturn(new ArrayList<>(Collections.singleton(booking)));
        when(itemRepository.getReferenceById(any()))
                .thenReturn(item);
        when(userRepository.getReferenceById(any()))
                .thenReturn(user);

        final List<BookingInfoDto> bookingInfoDtoList = bookingService.getAllBookingsByOwnerId(
                1L, "WAITING", PageRequest.ofSize(10));

        assertEquals(new ArrayList<>(Collections.singleton(bookingInfoDto)), bookingInfoDtoList);
    }

    @Test
    void getAllCurrentBookingsByOwnerIdTest() {
        when(bookingRepository.getAllBookingsByOwnerId(anyLong(), any(PageRequest.class)))
                .thenReturn(new ArrayList<>(Collections.singleton(bookingCurrent)));
        when(itemRepository.getReferenceById(any()))
                .thenReturn(item);
        when(userRepository.getReferenceById(any()))
                .thenReturn(user);

        final List<BookingInfoDto> bookingInfoDtoList = bookingService.getAllBookingsByOwnerId(
                1L, "CURRENT", PageRequest.ofSize(10));

        assertEquals("CURRENT", bookingInfoDtoList.get(0).getStatus().toString());
    }

    @Test
    void getAllFutureBookingsByOwnerIdTest() {
        when(bookingRepository.getAllBookingsByOwnerId(anyLong(), any(PageRequest.class)))
                .thenReturn(new ArrayList<>(Collections.singleton(booking)));
        when(itemRepository.getReferenceById(any()))
                .thenReturn(item);
        when(userRepository.getReferenceById(any()))
                .thenReturn(user);

        final List<BookingInfoDto> bookingInfoDtoList = bookingService.getAllBookingsByOwnerId(
                1L, "FUTURE", PageRequest.ofSize(10));

        assertEquals(bookingInfoDto, bookingInfoDtoList.get(0));
    }

    @Test
    void getAllPastBookingsByOwnerIdTest() {
        when(bookingRepository.getAllBookingsByOwnerId(anyLong(), any(PageRequest.class)))
                .thenReturn(new ArrayList<>(Collections.singleton(bookingPast)));
        when(itemRepository.getReferenceById(any()))
                .thenReturn(item);
        when(userRepository.getReferenceById(any()))
                .thenReturn(user);

        final List<BookingInfoDto> bookingInfoDtoList = bookingService.getAllBookingsByOwnerId(
                1L, "PAST", PageRequest.ofSize(10));

        assertEquals(bookingInfoDtoPast, bookingInfoDtoList.get(0));
    }
}
