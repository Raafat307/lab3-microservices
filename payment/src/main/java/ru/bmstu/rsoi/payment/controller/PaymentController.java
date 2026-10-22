package ru.bmstu.rsoi.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bmstu.rsoi.payment.dto.PaymentRequest;
import ru.bmstu.rsoi.payment.dto.PaymentResponse;
import ru.bmstu.rsoi.payment.service.PaymentService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody PaymentRequest request) {
        return ResponseEntity.ok(service.createPayment(request.getPrice()));
    }

    @GetMapping("/{uid}")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable UUID uid) {
        return ResponseEntity.ok(service.getPayment(uid));
    }

    @PostMapping("/{uid}/cancel")
    public ResponseEntity<PaymentResponse> cancelPayment(@PathVariable UUID uid) {
        return ResponseEntity.ok(service.cancelPayment(uid));
    }
}