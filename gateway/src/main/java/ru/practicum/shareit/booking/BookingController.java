package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.exception.BadRequestException;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;

	@PostMapping
	public ResponseEntity<Object> addBooking(@Validated({Create.class}) @RequestBody BookingDto bookingDto,
									 @RequestHeader(value = "X-Sharer-User-Id", required = true) Long bookerId) {
		log.info("Request endpoint: 'POST /bookings' (новое бронирование {} пользователем {})", bookingDto, bookerId);
		return bookingClient.saveBooking(bookingDto, bookerId);
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> approveBookingById(@PathVariable Long bookingId,
											 @RequestHeader(value = "X-Sharer-User-Id", required = true) Long ownerId,
											 @RequestParam(value = "approved", required = true) Boolean approved) {
		log.info("Request endpoint: PATCH /bookings/{bookingId}?approved=" +
				" (Изменение статуса бронирования {} пользователем {}", bookingId, ownerId);
		return bookingClient.approveBookingById(bookingId, ownerId, approved);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
											 @PathVariable Long bookingId) {
		log.info("Get booking {}, userId={}", bookingId, userId);
		return bookingClient.getBooking(userId, bookingId);
	}

	@GetMapping
	public ResponseEntity<Object> getAllBookingsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId,
			@RequestParam(name = "state", defaultValue = "all") String stateParam,
			@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
			@Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new BadRequestException("Unknown state: " + stateParam));
		log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
		return bookingClient.getAllBookingsByUserId(userId, state, from, size);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getAllBookingsByOwnerId(@RequestHeader("X-Sharer-User-Id") Long ownerId,
								@RequestParam(name = "state", defaultValue = "all") String stateParam,
								@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
								@Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new BadRequestException("Unknown state: " + stateParam));
		log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, ownerId, from, size);
		return bookingClient.getAllBookingsByOwnerId(ownerId, state, from, size);
	}


//	@PostMapping
//	public ResponseEntity<Object> bookItem(@RequestHeader("X-Sharer-User-Id") long userId,
//			@RequestBody @Valid BookItemRequestDto requestDto) {
//		log.info("Creating booking {}, userId={}", requestDto, userId);
//		return bookingClient.bookItem(userId, requestDto);
//	}
}
