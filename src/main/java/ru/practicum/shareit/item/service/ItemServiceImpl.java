package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.exceptionType.NotFoundException;

import ru.practicum.shareit.exception.exceptionType.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;


import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;


    @Override
    public ItemResponseDto getItemById(Long userId, Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Вещь с id = " + id + " не найдена"));

        List<CommentResponseDto> comments = commentRepository.findByItemIdOrderByCreatedDesc(id)
                .stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList());


        if (item.getOwnerId().equals(userId)) {
            BookingShortDto lastBooking = getLastBooking(id);
            BookingShortDto nextBooking = getNextBooking(id);
            return ItemMapper.toDto(item, lastBooking, nextBooking, comments);
        }

        return ItemMapper.toDto(item, null, null, comments);
    }

    @Override
    public List<ItemResponseDto> getAllItems(Long userId) {
        List<Item> items = itemRepository.findAllByOwnerId(userId);

        if (items.isEmpty()) {
            return List.of();
        }

        List<Long> itemIds = items.stream().map(Item::getId).collect(Collectors.toList());

        Map<Long, List<CommentResponseDto>> commentsMap = commentRepository
                .findByItemIdInOrderByCreatedDesc(itemIds)
                .stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.groupingBy(comment ->
                        items.stream()
                                .filter(item -> commentRepository.findByItemIdOrderByCreatedDesc(item.getId())
                                        .stream()
                                        .anyMatch(c -> c.getId().equals(comment.getId())))
                                .findFirst()
                                .map(Item::getId)
                                .orElse(null)));

        return items.stream()
                .map(item -> {
                    BookingShortDto lastBooking = getLastBooking(item.getId());
                    BookingShortDto nextBooking = getNextBooking(item.getId());
                    List<CommentResponseDto> comments = commentRepository
                            .findByItemIdOrderByCreatedDesc(item.getId())
                            .stream()
                            .map(CommentMapper::toDto)
                            .collect(Collectors.toList());
                    return ItemMapper.toDto(item, lastBooking, nextBooking, comments);
                })
                .collect(Collectors.toList());
    }

    @Override
    public ItemResponseDto addNewItem(Long userId, ItemRequestDto itemDto) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }

        Item item = ItemMapper.toEntity(itemDto);

        item.setOwnerId(userId);

        Item saved = itemRepository.save(item);
        return ItemMapper.toDto(saved);
    }

    @Override
    public ItemResponseDto updateItem(Long userId, Long id, ItemRequestDto itemDto) {

        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Вещь с id = " + id + " не найдена"));
        if (!item.getOwnerId().equals(userId)) {
            throw new NotFoundException("Вещь с id = " + id + " не найдена для пользователя с id = " + userId);
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        Item updated = itemRepository.save(item);
        return ItemMapper.toDto(updated);
    }

    @Override
    public void deleteItemById(Long id) {
        Optional<Item> item = itemRepository.findById(id);
        if (item.isEmpty()) {
            throw new NotFoundException("Вещь с id = " + id + " не найдена");
        }
        itemRepository.deleteById(id);
    }

    @Override
    public List<ItemResponseDto> searchItem(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        List<Item> items = itemRepository
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(text, text);

        return items.stream()
                .filter(Item::getAvailable)
                .map(ItemMapper::toDto)
                .toList();
    }

    @Override
    public CommentResponseDto addComment(Long userId, Long itemId, CommentRequestDto commentDto) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id = " + itemId + " не найдена"));

        LocalDateTime now = LocalDateTime.now();
        boolean hasCompletedBooking = bookingRepository
                .existsByBookerIdAndItemIdAndStatusAndEndBefore(userId, itemId, BookingStatus.APPROVED, now);

        if (!hasCompletedBooking) {
            throw new ValidationException("Вы не можете оставить отзыв на вещь, которую не брали в аренду");
        }

        Comment comment = CommentMapper.toEntity(commentDto, item, author);
        Comment saved = commentRepository.save(comment);

        return CommentMapper.toDto(saved);
    }

    private BookingShortDto getLastBooking(Long itemId) {
        LocalDateTime now = LocalDateTime.now();
        Booking booking = bookingRepository.findTopByItemIdAndStatusAndStartBeforeOrderByStartDesc(itemId, BookingStatus.APPROVED, now);

        if (booking == null) {
            return null;
        }

        return new BookingShortDto(
                booking.getId(),
                booking.getBooker().getId(),
                booking.getStart(),
                booking.getEnd()
        );
    }

    private BookingShortDto getNextBooking(Long itemId) {
        LocalDateTime now = LocalDateTime.now();
        Booking booking = bookingRepository.findTopByItemIdAndStatusAndStartAfterOrderByStartAsc(itemId, BookingStatus.APPROVED, now);

        if (booking == null) {
            return null;
        }

        return new BookingShortDto(
                booking.getId(),
                booking.getBooker().getId(),
                booking.getStart(),
                booking.getEnd()
        );
    }
}
