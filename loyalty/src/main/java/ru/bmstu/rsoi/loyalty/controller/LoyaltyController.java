package ru.bmstu.rsoi.loyalty.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bmstu.rsoi.loyalty.dto.LoyaltyInfoRequest;
import ru.bmstu.rsoi.loyalty.dto.LoyaltyInfoResponse;
import ru.bmstu.rsoi.loyalty.service.LoyaltyService;

@RestController
@RequestMapping("/api/v1/loyalty")
@RequiredArgsConstructor
public class LoyaltyController {

    private final LoyaltyService service;

    @GetMapping
    public ResponseEntity<LoyaltyInfoResponse> getLoyalty(@RequestParam String username) {
        return ResponseEntity.ok(service.getLoyaltyInfo(username));
    }

    @PostMapping
    public ResponseEntity<LoyaltyInfoResponse> createLoyalty(@RequestBody LoyaltyInfoRequest request) {
        return ResponseEntity.ok(service.createDefault(request.getUsername()) != null
                ? service.getLoyaltyInfo(request.getUsername())
                : null);
    }

    @PostMapping("/increment")
    public ResponseEntity<LoyaltyInfoResponse> increment(@RequestBody LoyaltyInfoRequest request) {
        return ResponseEntity.ok(service.increment(request.getUsername()));
    }

    @PostMapping("/decrement")
    public ResponseEntity<LoyaltyInfoResponse> decrement(@RequestBody LoyaltyInfoRequest request) {
        return ResponseEntity.ok(service.decrement(request.getUsername()));
    }
}