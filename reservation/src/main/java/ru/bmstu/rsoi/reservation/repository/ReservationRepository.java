package ru.bmstu.rsoi.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bmstu.rsoi.reservation.entity.Reservation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findAllByUsername(String username);
    Optional<Reservation> findByReservationUid(UUID reservationUid);
}