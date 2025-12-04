package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.enums.BookingState;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;


import ru.practicum.shareit.exception.exceptionType.NotFoundException;
import ru.practicum.shareit.exception.exceptionType.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;


import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

    @Override
    public BookingResponseDto addNewReservation(Long userId, BookingRequestDto bookingDto) {


        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещь не найден"));

        if (!item.getAvailable()) {
            throw new ValidationException("Вещь недоступна");
        }
        if (item.getOwnerId().equals(userId)) {
            throw new NotFoundException("Нельзя забронировать свою вещь");
        }

        Booking booking = BookingMapper.toEntity(bookingDto, item, booker);
        bookingRepository.save(booking);

        return BookingMapper.toDto(booking);
    }


    @Override
    public BookingResponseDto changeState(Long userId, Long bookingId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ValidationException("Бронирование с id = " + bookingId + " не найдено"));

        Item item = itemRepository.findById(booking.getItem().getId())
                .orElseThrow(() -> new ValidationException("Вещь не найдена"));

        if (!item.getOwnerId().equals(userId)) {
            throw new ValidationException("Только владелец вещи может подтвердить бронирование");
        }
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }

        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new ValidationException("Бронирование уже обработано");
        }

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        Booking updated = bookingRepository.save(booking);

        return BookingMapper.toDto(updated);
    }

    @Override
    public BookingResponseDto getBookingById(Long userId, Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Бронирование с id = " + id + " не найдено"));
        boolean isBooker = booking.getBooker().getId().equals(userId);
        boolean isOwner = booking.getItem().getOwnerId().equals(userId);

        if (!isBooker && !isOwner) {
            throw new NotFoundException("Бронирование с id = " + id + " не найдено");
        }
        return BookingMapper.toDto(booking);
    }

    @Override
    public List<BookingResponseDto> getBookingsByUser(Long userId, BookingState state, Integer from, Integer size) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }

        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, Sort.by("start").descending());
        LocalDateTime now = LocalDateTime.now();

        Page<Booking> pageResult;

        switch (state) {
            case ALL:
                pageResult = bookingRepository.findByBooker_IdOrderByStartDesc(userId, pageable);
                break;
            case CURRENT:
                pageResult = bookingRepository.findByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(userId, now, now, pageable);
                break;
            case PAST:
                pageResult = bookingRepository.findByBooker_IdAndEndBeforeOrderByStartDesc(userId, now, pageable);
                break;
            case FUTURE:
                pageResult = bookingRepository.findByBooker_IdAndStartAfterOrderByStartDesc(userId, now, pageable);
                break;
            case WAITING:
                pageResult = bookingRepository.findByBooker_IdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING, pageable);
                break;
            case REJECTED:
                pageResult = bookingRepository.findByBooker_IdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED, pageable);
                break;
            default:
                throw new ValidationException("Неизвестный статус: " + state);
        }

        return pageResult.stream()
                .map(BookingMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponseDto> getBookingsByOwner(Long ownerId, BookingState state, Integer from, Integer size) {
        if (!userRepository.existsById(ownerId)) {
            throw new NotFoundException("Пользователь с id = " + ownerId + " не найден");
        }

        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, Sort.by("start").descending());
        LocalDateTime now = LocalDateTime.now();

        Page<Booking> pageResult;

        switch (state) {
            case ALL:
                pageResult = bookingRepository.findByItem_OwnerIdOrderByStartDesc(ownerId, pageable);
                break;
            case CURRENT:
                pageResult = bookingRepository.findByItem_OwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(ownerId, now, now, pageable);
                break;
            case PAST:
                pageResult = bookingRepository.findByItem_OwnerIdAndEndBeforeOrderByStartDesc(ownerId, now, pageable);
                break;
            case FUTURE:
                pageResult = bookingRepository.findByItem_OwnerIdAndStartAfterOrderByStartDesc(ownerId, now, pageable);
                break;
            case WAITING:
                pageResult = bookingRepository.findByItem_OwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.WAITING, pageable);
                break;
            case REJECTED:
                pageResult = bookingRepository.findByItem_OwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.REJECTED, pageable);
                break;
            default:
                throw new ValidationException("Неизвестный статус: " + state);
        }

        return pageResult.stream()
                .map(BookingMapper::toDto)
                .collect(Collectors.toList());
    }
}
