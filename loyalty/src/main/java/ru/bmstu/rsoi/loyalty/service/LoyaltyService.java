package ru.bmstu.rsoi.loyalty.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bmstu.rsoi.loyalty.dto.LoyaltyInfoResponse;
import ru.bmstu.rsoi.loyalty.entity.Loyalty;
import ru.bmstu.rsoi.loyalty.repository.LoyaltyRepository;

@Service
@RequiredArgsConstructor
public class LoyaltyService {

    private final LoyaltyRepository repository;

    // جلب معلومات الولاء لمستخدم
    public LoyaltyInfoResponse getLoyaltyInfo(String username) {
        Loyalty loyalty = repository.findByUsername(username)
                .orElseGet(() -> createDefault(username));
        return toResponse(loyalty);
    }

    // إنشاء سجل ولاء افتراضي إذا لم يكن موجودًا
    @Transactional
    public Loyalty createDefault(String username) {
        Loyalty loyalty = new Loyalty();
        loyalty.setUsername(username);
        loyalty.setReservationCount(0);
        loyalty.setStatus("BRONZE");
        loyalty.setDiscount(5);
        return repository.save(loyalty);
    }

    // زيادة عدّاد الحجوزات وإعادة حساب الحالة
    @Transactional
    public LoyaltyInfoResponse increment(String username) {
        Loyalty loyalty = repository.findByUsername(username)
                .orElseGet(() -> createDefault(username));
        loyalty.setReservationCount(loyalty.getReservationCount() + 1);
        recalculateStatus(loyalty);
        repository.save(loyalty);
        return toResponse(loyalty);
    }

    // إنقاص عدّاد الحجوزات
    @Transactional
    public LoyaltyInfoResponse decrement(String username) {
        Loyalty loyalty = repository.findByUsername(username)
                .orElseGet(() -> createDefault(username));
        if (loyalty.getReservationCount() > 0) {
            loyalty.setReservationCount(loyalty.getReservationCount() - 1);
            recalculateStatus(loyalty);
            repository.save(loyalty);
        }
        return toResponse(loyalty);
    }

    // منطق الحالة والخصم
    private void recalculateStatus(Loyalty loyalty) {
        int count = loyalty.getReservationCount();
        if (count >= 20) {
            loyalty.setStatus("GOLD");
            loyalty.setDiscount(10);
        } else if (count >= 10) {
            loyalty.setStatus("SILVER");
            loyalty.setDiscount(7);
        } else {
            loyalty.setStatus("BRONZE");
            loyalty.setDiscount(5);
        }
    }

    private LoyaltyInfoResponse toResponse(Loyalty loyalty) {
        return new LoyaltyInfoResponse(
                loyalty.getStatus(),
                loyalty.getDiscount(),
                loyalty.getReservationCount()
        );
    }
}