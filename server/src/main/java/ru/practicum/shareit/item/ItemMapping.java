package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.comment.dto.CommentInfoDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ItemMapping {

    public ItemDto mapToItemDto(Item item) {
        return new ItemDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequestId());
    }

    public ItemInfoDto mapToItemInfoDto(Item item, Booking lBooking, Booking nBooking, List<CommentInfoDto> comments) {
        ItemInfoDto.BookingDto lastBooking;
        ItemInfoDto.BookingDto nextBooking;

        if (lBooking == null) {
            lastBooking = null;
        } else {
            lastBooking = new ItemInfoDto.BookingDto(lBooking.getId(),
                    lBooking.getStartDate(),
                    lBooking.getEndDate(),
                    lBooking.getBookerId());
        }

        if (nBooking == null) {
            nextBooking = null;
        } else {
            nextBooking = new ItemInfoDto.BookingDto(nBooking.getId(),
                    nBooking.getStartDate(),
                    nBooking.getEndDate(),
                    nBooking.getBookerId());
        }
        return new ItemInfoDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getOwnerId(),
                item.getAvailable(),
                lastBooking,
                nextBooking,
                comments);
    }

    public Item mapToItem(ItemDto itemDto, long ownerId) {
        return new Item(itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                ownerId,
                itemDto.getRequestId());
    }
}
