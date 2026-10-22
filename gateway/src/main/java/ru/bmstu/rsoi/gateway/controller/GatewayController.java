package ru.bmstu.rsoi.gateway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bmstu.rsoi.gateway.dto.*;
import ru.bmstu.rsoi.gateway.service.GatewayService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class GatewayController {

    private final GatewayService gatewayService;

    // قائمة الفنادق
    @GetMapping("/hotels")
    public ResponseEntity<List<HotelResponse>> getAllHotels() {
        return ResponseEntity.ok(gatewayService.getAllHotels());
    }

    // حجوزات المستخدم
    @GetMapping("/reservations")
    public ResponseEntity<List<FullReservationResponse>> getUserReservations(@RequestParam String username) {
        return ResponseEntity.ok(gatewayService.getUserReservations(username));
    }

    // حجز محدد
    @GetMapping("/reservations/{reservationUid}")
    public ResponseEntity<FullReservationResponse> getReservation(@PathVariable UUID reservationUid) {
        return ResponseEntity.ok(gatewayService.getReservation(reservationUid));
    }

    // حجز فندق (السيناريو الكامل)
    @PostMapping("/reservations")
    public ResponseEntity<CreateReservationResponse> bookHotel(@RequestBody CreateReservationRequest request) {
        return ResponseEntity.ok(gatewayService.bookHotel(request));
    }

    // إلغاء حجز
    @DeleteMapping("/reservations/{reservationUid}")
    public ResponseEntity<Void> cancelReservation(@PathVariable UUID reservationUid) {
        gatewayService.cancelReservation(reservationUid);
        return ResponseEntity.noContent().build();
    }

    // معلومات المستخدم
    @GetMapping("/me")
    public ResponseEntity<?> getMe(@RequestParam String username) {
        return ResponseEntity.ok(gatewayService.getMe(username));
    }
}