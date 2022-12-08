package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;

@Service
@RequiredArgsConstructor
public class BookingMapping {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public BookingDto mapToBookingDto(Booking booking) {
        return new BookingDto(booking.getId(),
                booking.getItemId(),
                booking.getStartDate(),
                booking.getEndDate());
    }

    public Booking mapToBooking(BookingDto bookingDto, Long bookerId, Status status) {
        Booking booking = new Booking(bookingDto.getId(),
                bookingDto.getStart(),
                bookingDto.getEnd(),
                bookingDto.getItemId(),
                bookerId,
                status);
        return booking;
    }

    public BookingInfoDto mapToBookingInfoDto(Booking booking) {
        BookingInfoDto bookingInfoDto = new BookingInfoDto(
                booking.getId(),
                booking.getStartDate(),
                booking.getEndDate(),
                booking.getStatus(),
                booking.getBookerId(),
                booking.getItemId(),
                itemRepository.getReferenceById(booking.getItemId()).getName(),
                new BookingInfoDto.BookerDto(
                        userRepository.getReferenceById(booking.getBookerId()).getId(),
                        userRepository.getReferenceById(booking.getBookerId()).getName()),
                new BookingInfoDto.ItemDto(
                        itemRepository.getReferenceById(booking.getItemId()).getId(),
                        itemRepository.getReferenceById(booking.getItemId()).getName(),
                        itemRepository.getReferenceById(booking.getItemId()).getOwnerId()));
        return bookingInfoDto;
    }
}
