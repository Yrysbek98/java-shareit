package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
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
        if (bookingDto.getStart() == null) {
            throw new ValidationException("Дата начала бронирования не может быть пустой");
        }

        if (bookingDto.getEnd() == null) {
            throw new ValidationException("Дата окончания бронирования не может быть пустой");
        }

        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new ValidationException("Дата окончания не может быть раньше даты начала");
        }

        if (bookingDto.getEnd().isEqual(bookingDto.getStart())) {
            throw new ValidationException("Дата окончания не может совпадать с датой начала");
        }

        LocalDateTime now = LocalDateTime.now();
        if (bookingDto.getStart().isBefore(now)) {
            throw new ValidationException("Дата начала бронирования не может быть в прошлом");
        }

        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Item not found"));

        if (!item.getAvailable()) {
            throw new ValidationException("Item is not available for booking");
        }

        if (bookingDto.getEnd().isBefore(bookingDto.getStart()) || bookingDto.getEnd().isEqual(bookingDto.getStart())) {
            throw new ValidationException("End date must be after start date");
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
    public BookingResponseDto getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Бронирование с id = " + id + " не найдено"));
        return BookingMapper.toDto(booking);
    }

    @Override
    public List<BookingResponseDto> getBookingsByUser(Long userId, BookingState state) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }

        List<Booking> bookings;
        LocalDateTime now = LocalDateTime.now();

        switch (state) {
            case ALL:
                bookings = bookingRepository.findByBooker_IdOrderByStartDesc(userId);
                break;

            case CURRENT:
                bookings = bookingRepository.findByBooker_IdOrderByStartDesc(userId).stream()
                        .filter(b -> b.getStart().isBefore(now) && b.getEnd().isAfter(now))
                        .collect(Collectors.toList());
                break;

            case PAST:
                bookings = bookingRepository.findByBooker_IdOrderByStartDesc(userId).stream()
                        .filter(b -> b.getEnd().isBefore(now))
                        .collect(Collectors.toList());
                break;

            case FUTURE:
                bookings = bookingRepository.findByBooker_IdOrderByStartDesc(userId).stream()
                        .filter(b -> b.getStart().isAfter(now))
                        .collect(Collectors.toList());
                break;

            case WAITING:
                bookings = bookingRepository.findByBooker_IdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING);
                break;

            case REJECTED:
                bookings = bookingRepository.findByBooker_IdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED);
                break;

            default:
                throw new ValidationException("Неизвестный статус: " + state);
        }

        return bookings.stream()
                .map(BookingMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponseDto> getBookingsByOwner(Long ownerId, BookingState state) {
        if (!userRepository.existsById(ownerId)) {
            throw new NotFoundException("Пользователь с id = " + ownerId + " не найден");
        }

        List<Booking> bookings;
        LocalDateTime now = LocalDateTime.now();

        switch (state) {
            case ALL:
                bookings = bookingRepository.findByItem_OwnerIdOrderByStartDesc(ownerId);
                break;

            case CURRENT:
                bookings = bookingRepository.findByItem_OwnerIdOrderByStartDesc(ownerId).stream()
                        .filter(b -> b.getStart().isBefore(now) && b.getEnd().isAfter(now))
                        .collect(Collectors.toList());
                break;

            case PAST:
                bookings = bookingRepository.findByItem_OwnerIdOrderByStartDesc(ownerId).stream()
                        .filter(b -> b.getEnd().isBefore(now))
                        .collect(Collectors.toList());
                break;

            case FUTURE:
                bookings = bookingRepository.findByItem_OwnerIdOrderByStartDesc(ownerId).stream()
                        .filter(b -> b.getStart().isAfter(now))
                        .collect(Collectors.toList());
                break;

            case WAITING:
                bookings = bookingRepository.findByItem_OwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.WAITING);
                break;

            case REJECTED:
                bookings = bookingRepository.findByItem_OwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.REJECTED);
                break;

            default:
                throw new ValidationException("Неизвестный статус: " + state);
        }

        return bookings.stream()
                .map(BookingMapper::toDto)
                .collect(Collectors.toList());


    }
}
