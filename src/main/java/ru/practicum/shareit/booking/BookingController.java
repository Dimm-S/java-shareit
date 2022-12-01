package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingInfoDto addBooking(@Validated({Create.class}) @RequestBody BookingDto bookingDto,
                                 @RequestHeader(value = "X-Sharer-User-Id", required = true) Long bookerId) {
        log.info("Request endpoint: 'POST /bookings' (новое бронирование {} пользователем {})", bookingDto, bookerId);
        return bookingService.saveBooking(bookingDto, bookerId);
    }

    @PatchMapping("/{bookingId}")
    public BookingInfoDto approveBookingById(@PathVariable Long bookingId,
                                             @RequestHeader(value = "X-Sharer-User-Id", required = true) Long ownerId,
                                             @RequestParam(value = "approved", required = true) Boolean approved) {
        log.info("Request endpoint: PATCH /bookings/{bookingId}?approved=" +
                " (Изменение статуса бронирования {} пользователем {}", bookingId, ownerId);
        return bookingService.approveBookingById(bookingId, ownerId, approved);
    }

    @GetMapping("/{id}")
    public BookingInfoDto getBooking(@PathVariable Long id,
                                     @RequestHeader(value = "X-Sharer-User-Id", required = true) Long userId) {
        log.info("Request endpoint: 'GET /bookings/{}' (Получение бронирования по id)", id);
        return bookingService.getBookingById(id, userId);
    }

    @GetMapping
    public List<BookingInfoDto> getAllBookingsByUserId(
            @RequestHeader(value = "X-Sharer-User-Id", required = true) Long userId,
            @RequestParam(value = "state", required = false, defaultValue = "ALL") String state,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Request endpoint: 'GET /bookings?state'" +
                " (Получение всех бронирований пользователя {} со статусом {})", userId, state);
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        return bookingService.getAllBookingsByUserId(userId, state, pageRequest);
    }

    @GetMapping("/owner")
    public List<BookingInfoDto> getAllBookingsByOwnerId(
            @RequestHeader(value = "X-Sharer-User-Id", required = true) Long ownerId,
            @RequestParam(value = "state", required = false, defaultValue = "ALL") String state,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Request endpoint: 'GET /bookings/owner?state'" +
                " Получение всех бронирований владельца {} со статусом {}", ownerId, state);
        int page = from / size;
        final PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        return bookingService.getAllBookingsByOwnerId(ownerId, state, pageRequest);
    }
}
