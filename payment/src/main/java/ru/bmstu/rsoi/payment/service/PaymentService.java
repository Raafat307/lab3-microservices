package ru.bmstu.rsoi.payment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bmstu.rsoi.payment.dto.PaymentResponse;
import ru.bmstu.rsoi.payment.entity.Payment;
import ru.bmstu.rsoi.payment.repository.PaymentRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository repository;

    // إنشاء دفعة جديدة
    @Transactional
    public PaymentResponse createPayment(Integer price) {
        Payment payment = new Payment();
        payment.setPaymentUid(UUID.randomUUID());
        payment.setStatus("PAID");
        payment.setPrice(price);
        repository.save(payment);
        return toResponse(payment);
    }

    // جلب دفعة
    public PaymentResponse getPayment(UUID paymentUid) {
        Payment payment = repository.findByPaymentUid(paymentUid)
                .orElseThrow(() -> new RuntimeException("Payment not found: " + paymentUid));
        return toResponse(payment);
    }

    // إلغاء دفعة
    @Transactional
    public PaymentResponse cancelPayment(UUID paymentUid) {
        Payment payment = repository.findByPaymentUid(paymentUid)
                .orElseThrow(() -> new RuntimeException("Payment not found: " + paymentUid));
        payment.setStatus("CANCELED");
        repository.save(payment);
        return toResponse(payment);
    }

    private PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
                payment.getPaymentUid(),
                payment.getStatus(),
                payment.getPrice()
        );
    }
}