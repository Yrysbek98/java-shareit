package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;


import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBooker_IdOrderByStartDesc(Long bookerId);


    List<Booking> findByItem_OwnerIdOrderByStartDesc(Long ownerId);


    List<Booking> findByBooker_IdAndStatusOrderByStartDesc(Long bookerId, BookingStatus status);


    List<Booking> findByItem_OwnerIdAndStatusOrderByStartDesc(Long ownerId, BookingStatus status);

    Booking findTopByItemIdAndStatusAndStartBeforeOrderByStartDesc(
            Long itemId,
            BookingStatus status,
            LocalDateTime now
    );

    Booking findTopByItemIdAndStatusAndStartAfterOrderByStartAsc(
            Long itemId,
            BookingStatus status,
            LocalDateTime now
    );

    boolean existsByBookerIdAndItemIdAndStatusAndEndBefore(
            Long bookerId,
            Long itemId,
            BookingStatus status,
            LocalDateTime time
    );
}


