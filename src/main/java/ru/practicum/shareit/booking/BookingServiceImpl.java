package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final BookingMapping bookingMapping;
    private final ItemService itemService;

    @Override
    public BookingInfoDto saveBooking(BookingDto bookingDto, Long bookerId) {
        userService.checkUserExistence(bookerId);
        if (itemService.getItemById(bookingDto.getItemId(), bookerId).getOwnerId().equals(bookerId)) {
            throw new NotFoundException("User can't booking own item");
        }
        itemService.checkItemAvailable(bookingDto);
        return bookingMapping.mapToBookingInfoDto(
                bookingRepository.save(
                        bookingMapping.mapToBooking(bookingDto, bookerId, Status.WAITING)));
    }

    @Override
    public BookingInfoDto approveBookingById(Long bookingId, Long ownerId, Boolean approved) {
        Booking booking = bookingRepository.getReferenceById(bookingId);
        checkOwnerOfItem(booking, ownerId);
        checkCurrentStatus(booking, bookingId);
        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        bookingRepository.save(booking);
        return bookingMapping.mapToBookingInfoDto(booking);
    }

    @Override
    public BookingInfoDto getBookingById(Long id, Long userId) {
        checkBooking(id, userId);
        return bookingMapping.mapToBookingInfoDto(bookingRepository.getReferenceById(id));
    }

    @Override
    public List<BookingInfoDto> getAllBookingsByUserId(Long userId, String state) {
        userService.checkUserExistence(userId);
        checkState(state);
        Status status = Status.valueOf(state);
        List<Booking> bookingList = bookingRepository.findAllBookingsByBookerId(userId);
        if (Status.ALL.equals(status)) {
            return bookingList.stream()
                    .sorted(Comparator.comparing((Booking::getStartDate)).reversed())
                    .map(bookingMapping::mapToBookingInfoDto)
                    .collect(Collectors.toList());
        }
        if (Status.FUTURE.equals(status)) {
            return bookingList.stream()
                    .filter(booking -> booking.getStartDate().isAfter(LocalDateTime.now()))
                    .sorted(Comparator.comparing((Booking::getStartDate)).reversed())
                    .map(bookingMapping::mapToBookingInfoDto)
                    .collect(Collectors.toList());
        }
        if (Status.CURRENT.equals(status)) {
            return bookingList.stream()
                    .filter(booking -> booking.getStartDate().isBefore(LocalDateTime.now()))
                    .filter(booking -> booking.getEndDate().isAfter(LocalDateTime.now()))
                    .sorted(Comparator.comparing((Booking::getStartDate)).reversed())
                    .map(bookingMapping::mapToBookingInfoDto)
                    .collect(Collectors.toList());
        }
        if (Status.PAST.equals(status)) {
            return bookingList.stream()
                    .filter(booking -> booking.getEndDate().isBefore(LocalDateTime.now()))
                    .sorted(Comparator.comparing((Booking::getStartDate)).reversed())
                    .map(bookingMapping::mapToBookingInfoDto)
                    .collect(Collectors.toList());
        }
        return bookingList.stream()
                .filter(booking -> booking.getStatus().equals(status))
                .map(bookingMapping::mapToBookingInfoDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingInfoDto> getAllBookingsByOwnerId(Long ownerId, String state) {
        userService.checkUserExistence(ownerId);
        checkState(state);
        Status status = Status.valueOf(state);
        List<Booking> bookingList = bookingRepository.getAllBookingsByOwnerId(ownerId);
        if (Status.ALL.equals(status)) {
            return bookingList.stream()
                    .sorted(Comparator.comparing((Booking::getStartDate)).reversed())
                    .map(bookingMapping::mapToBookingInfoDto)
                    .collect(Collectors.toList());
        }
        if (Status.FUTURE.equals(status)) {
            return bookingList.stream()
                    .filter(booking -> booking.getStartDate().isAfter(LocalDateTime.now()))
                    .sorted(Comparator.comparing((Booking::getStartDate)).reversed())
                    .map(bookingMapping::mapToBookingInfoDto)
                    .collect(Collectors.toList());
        }
        if (Status.CURRENT.equals(status)) {
            return bookingList.stream()
                    .filter(booking -> booking.getStartDate().isBefore(LocalDateTime.now()))
                    .filter(booking -> booking.getEndDate().isAfter(LocalDateTime.now()))
                    .sorted(Comparator.comparing((Booking::getStartDate)).reversed())
                    .map(bookingMapping::mapToBookingInfoDto)
                    .collect(Collectors.toList());
        }
        if (Status.PAST.equals(status)) {
            return bookingList.stream()
                    .filter(booking -> booking.getEndDate().isBefore(LocalDateTime.now()))
                    .sorted(Comparator.comparing((Booking::getStartDate)).reversed())
                    .map(bookingMapping::mapToBookingInfoDto)
                    .collect(Collectors.toList());
        }
        return bookingList.stream()
                .filter(booking -> booking.getStatus().equals(status))
                .map(bookingMapping::mapToBookingInfoDto)
                .collect(Collectors.toList());
    }

    private void checkBooking(Long id, Long userId) {
        if (!bookingRepository.existsById(id)) {
            throw new NotFoundException("Booking with id=" + id + " not found");
        }
        var booking = bookingMapping.mapToBookingInfoDto(bookingRepository.getReferenceById(id));
        if (!booking.getBookerId().equals(userId) && !booking.getItem().getOwnerId().equals(userId)) {
            throw new NotFoundException("User " + userId + " not creator or owner booking " + id);
        }
    }

    private void checkState(String state) {
        if (Arrays.stream(Status.values()).noneMatch(status1 -> status1.toString().equals(state))) {
            throw new BadRequestException("Unknown state: " + state);
        }
    }

    private void checkOwnerOfItem(Booking booking, Long ownerId) {
        if (!bookingMapping.mapToBookingInfoDto(booking).getItem().getOwnerId().equals(ownerId)) {
            throw new NotFoundException("User " + ownerId + " is not owner of item " + booking.getItemId());
        }
    }

    private void checkCurrentStatus(Booking booking, Long bookingId) {
        if (booking.getStatus().equals(Status.APPROVED)) {
            throw new BadRequestException("Booking " + bookingId + " already approved");
        }
    }
}
