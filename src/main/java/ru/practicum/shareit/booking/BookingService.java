package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;

import java.util.List;

public interface BookingService {
    BookingDto saveBooking(BookingDto bookingDto, Long bookerId);

    BookingInfoDto approveBookingById(Long bookingId, Long ownerId, Boolean approved);

    BookingInfoDto getBookingById(Long id, Long userId);

    List<BookingInfoDto> getAllBookingsByUserId(Long userId, String status);

    List<BookingInfoDto> getAllBookingsByOwnerId(Long ownerId, String status);
}
