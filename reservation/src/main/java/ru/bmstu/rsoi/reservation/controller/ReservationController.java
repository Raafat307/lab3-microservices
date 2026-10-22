package ru.bmstu.rsoi.reservation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bmstu.rsoi.reservation.dto.*;
import ru.bmstu.rsoi.reservation.service.ReservationService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService service;

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getUserReservations(@RequestParam String username) {
        return ResponseEntity.ok(service.getUserReservations(username));
    }

    @GetMapping("/{reservationUid}")
    public ResponseEntity<ReservationResponse> getReservation(@PathVariable UUID reservationUid) {
        return ResponseEntity.ok(service.getReservation(reservationUid));
    }

    @PostMapping
    public ResponseEntity<CreateReservationResponse> createReservation(@RequestBody ReservationRequest request) {
        return ResponseEntity.ok(service.createReservation(request));
    }

    @DeleteMapping("/{reservationUid}")
    public ResponseEntity<Void> cancelReservation(@PathVariable UUID reservationUid) {
        service.cancelReservation(reservationUid);
        return ResponseEntity.noContent().build();
    }
}