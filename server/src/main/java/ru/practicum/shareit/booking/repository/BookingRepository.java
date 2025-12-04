package ru.practicum.shareit.booking.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;


import java.time.LocalDateTime;


@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Page<Booking> findByBooker_IdOrderByStartDesc(Long bookerId, Pageable pageable);

    Page<Booking> findByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(
            Long bookerId, LocalDateTime startBefore, LocalDateTime endAfter, Pageable pageable);

    Page<Booking> findByBooker_IdAndEndBeforeOrderByStartDesc(Long bookerId, LocalDateTime endBefore, Pageable pageable);

    Page<Booking> findByBooker_IdAndStartAfterOrderByStartDesc(Long bookerId, LocalDateTime startAfter, Pageable pageable);

    Page<Booking> findByBooker_IdAndStatusOrderByStartDesc(Long bookerId, BookingStatus status, Pageable pageable);

    Page<Booking> findByItem_OwnerIdOrderByStartDesc(Long ownerId, Pageable pageable);

    Page<Booking> findByItem_OwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(
            Long ownerId, LocalDateTime startBefore, LocalDateTime endAfter, Pageable pageable);

    Page<Booking> findByItem_OwnerIdAndEndBeforeOrderByStartDesc(Long ownerId, LocalDateTime endBefore, Pageable pageable);

    Page<Booking> findByItem_OwnerIdAndStartAfterOrderByStartDesc(Long ownerId, LocalDateTime startAfter, Pageable pageable);

    Page<Booking> findByItem_OwnerIdAndStatusOrderByStartDesc(Long ownerId, BookingStatus status, Pageable pageable);

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


