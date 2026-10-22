package ru.bmstu.rsoi.reservation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bmstu.rsoi.reservation.dto.HotelResponse;
import ru.bmstu.rsoi.reservation.service.ReservationService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final ReservationService service;

    @GetMapping
    public ResponseEntity<List<HotelResponse>> getAllHotels() {
        return ResponseEntity.ok(service.getAllHotels());
    }

    @GetMapping("/{hotelUid}")
    public ResponseEntity<HotelResponse> getHotel(@PathVariable UUID hotelUid) {
        return ResponseEntity.ok(service.getHotel(hotelUid));
    }
}