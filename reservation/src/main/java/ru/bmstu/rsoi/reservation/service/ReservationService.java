package ru.bmstu.rsoi.reservation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bmstu.rsoi.reservation.dto.*;
import ru.bmstu.rsoi.reservation.entity.Hotel;
import ru.bmstu.rsoi.reservation.entity.Reservation;
import ru.bmstu.rsoi.reservation.repository.HotelRepository;
import ru.bmstu.rsoi.reservation.repository.ReservationRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final HotelRepository hotelRepository;
    private final ReservationRepository reservationRepository;

    // قائمة الفنادق
    public List<HotelResponse> getAllHotels() {
        return hotelRepository.findAll().stream()
                .map(this::toHotelResponse)
                .collect(Collectors.toList());
    }

    // فندق محدد
    public HotelResponse getHotel(UUID hotelUid) {
        Hotel hotel = hotelRepository.findByHotelUid(hotelUid)
                .orElseThrow(() -> new RuntimeException("Hotel not found: " + hotelUid));
        return toHotelResponse(hotel);
    }

    // حجوزات مستخدم
    public List<ReservationResponse> getUserReservations(String username) {
        return reservationRepository.findAllByUsername(username).stream()
                .map(this::toReservationResponse)
                .collect(Collectors.toList());
    }

    // حجز محدد
    public ReservationResponse getReservation(UUID reservationUid) {
        Reservation r = reservationRepository.findByReservationUid(reservationUid)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        return toReservationResponse(r);
    }

    // إنشاء حجز
    @Transactional
    public CreateReservationResponse createReservation(ReservationRequest request) {
        Hotel hotel = hotelRepository.findByHotelUid(request.getHotelUid())
                .orElseThrow(() -> new RuntimeException("Hotel not found: " + request.getHotelUid()));

        Reservation reservation = new Reservation();
        reservation.setReservationUid(UUID.randomUUID());
        reservation.setUsername(request.getUsername());
        reservation.setPaymentUid(request.getPaymentUid());
        reservation.setHotel(hotel);
        reservation.setStatus("PAID");
        reservation.setStartDate(request.getStartDate());
        reservation.setEndDate(request.getEndDate());
        reservationRepository.save(reservation);

        return new CreateReservationResponse(
                reservation.getReservationUid(),
                hotel.getHotelUid(),
                reservation.getStatus()
        );
    }

    // إلغاء حجز
    @Transactional
    public void cancelReservation(UUID reservationUid) {
        Reservation r = reservationRepository.findByReservationUid(reservationUid)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        r.setStatus("CANCELED");
        reservationRepository.save(r);
    }

    private HotelResponse toHotelResponse(Hotel h) {
        return new HotelResponse(
                h.getHotelUid(), h.getName(), h.getCountry(),
                h.getCity(), h.getAddress(), h.getStars(), h.getPrice()
        );
    }

    private ReservationResponse toReservationResponse(Reservation r) {
        return new ReservationResponse(
                r.getReservationUid(),
                r.getUsername(),
                r.getPaymentUid(),
                r.getHotel().getHotelUid(),
                r.getStatus(),
                r.getStartDate(),
                r.getEndDate()
        );
    }
}