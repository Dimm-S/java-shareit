package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(nativeQuery = true,
            value = "SELECT * " +
                    "FROM bookings AS b " +
                    "LEFT JOIN items AS it ON b.item_id = it.id " +
                    "WHERE it.owner_id = ?")
    List<Booking> getAllBookingsByOwnerId(Long ownerId);

    @Query
    List<Booking> findAllBookingsByBookerId(Long BookerId);

    @Query(nativeQuery = true,
            value = "SELECT * " +
                    "FROM bookings " +
                    "WHERE item_id = ?")
    List<Booking> getAllBookingsByItemId(Long itemId);

    @Query(nativeQuery = true,
            value = "SELECT * " +
                    "FROM bookings " +
                    "WHERE item_id = ? " +
                    "AND end_date < ?")
    List<Booking> findAllByItemIdAndEndDate(Long itemId, LocalDateTime now);
}
