package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapping itemMapping;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final CommentMapping commentMapping;

    @Override
    public ItemDto saveItem(ItemDto itemDto, Long ownerId) {
        userService.checkUserExistence(ownerId);
        return itemMapping.mapToItemDto(
                itemRepository.save(
                        itemMapping.mapToItem(itemDto, ownerId)));
    }

    @Override
    public CommentInfoDto saveComment(Long itemId, CommentDto commentDto, Long userId) {
        List<Long> bookerIds = bookingRepository.findAllByItemIdAndEndDate(itemId, LocalDateTime.now()).stream()
                .map(Booking::getBookerId)
                .collect(Collectors.toList());
        if (!bookerIds.contains(userId)) {
            throw new BadRequestException("User " + userId + " can't create comment for item " + itemId);
        }
        UserDto user = userService.getUserById(userId);
        return commentMapping.mapToCommentInfoDto(
                commentRepository.save(
                        commentMapping.mapToComment(commentDto, itemId, userId)
                ), user
        );
    }

    @Override
    public ItemInfoDto getItemById(Long id, Long userId) {
        checkItemExistence(id);
        List<Comment> comments = commentRepository.findAllByItemId(id);
        List<CommentInfoDto> commentInfoDtos = comments.stream()
                .map(comment -> commentMapping.mapToCommentInfoDto(comment,
                        userService.getUserById(comment.getAuthorId())))
                .collect(Collectors.toList());
        List<Booking> bookingList = bookingRepository.getAllBookingsByItemId(id);
        Booking lastBooking = bookingList.stream()
                .filter(booking -> booking.getStartDate().isBefore(LocalDateTime.now()))
                .max(Comparator.comparing(Booking::getStartDate)).orElse(null);
        Booking nextBooking = bookingList.stream()
                .filter(booking -> booking.getStartDate().isAfter(LocalDateTime.now()))
                .min(Comparator.comparing(Booking::getStartDate)).orElse(null);
        ItemInfoDto item = itemMapping.mapToItemInfoDto(itemRepository.getReferenceById(id),
                lastBooking, nextBooking, commentInfoDtos);
        if (!item.getOwnerId().equals(userId)) {
            item.setLastBooking(null);
            item.setNextBooking(null);
        }
        return item;
    }

    @Override
    public List<ItemInfoDto> getAllItems(Long userId) {
        List<Item> items = itemRepository.getAllItemsByUserId(userId);
        return items.stream()
                .map(Item::getId)
                .map(id -> getItemById(id, userId))
                .sorted(Comparator.comparing(ItemInfoDto::getId))
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto updateItem(Long id, ItemDto itemDto, Long ownerId) {
        checkItemExistence(id);
        checkOwner(id, ownerId);
        Item item = itemRepository.getReferenceById(id);
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        itemRepository.save(item);
        return itemMapping.mapToItemDto(item);
    }

    @Override
    public void deleteItem(Long id, Long ownerId) {
        checkItemExistence(id);
        checkOwner(id, ownerId);
        itemRepository.deleteById(id);
    }

    @Override
    public List<ItemDto> getItemsByQuery(String query) {
        if (query.equals("")) {
            return new ArrayList<>();
        }
        return itemRepository.findAll().stream()
                .filter(item -> item.getName().toLowerCase().contains(query.toLowerCase())
                        || item.getDescription().toLowerCase().contains(query.toLowerCase()))
                .filter(item -> item.getAvailable().equals(true))
                .map(itemMapping::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public void checkItemExistence(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new NotFoundException("Item with id=" + id + " not found");
        }
    }

    @Override
    public void checkItemAvailable(BookingDto bookingDto) {
        if (itemRepository.getReferenceById(bookingDto.getItemId()).getAvailable().equals(false)) {
            throw new BadRequestException("Item " + bookingDto.getItemId() + " is unavailable");
        }
    }

    private void checkOwner(Long itemId, Long ownerId) {
        if (ownerId != itemRepository.getReferenceById(itemId).getOwnerId()) {
            throw new NotFoundException("User with id=" + ownerId + " is not owner of item=" + itemId);
        }
    }
}
