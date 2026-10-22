package ru.bmstu.rsoi.gateway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bmstu.rsoi.gateway.client.LoyaltyClient;
import ru.bmstu.rsoi.gateway.client.PaymentClient;
import ru.bmstu.rsoi.gateway.client.ReservationClient;
import ru.bmstu.rsoi.gateway.dto.*;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GatewayService {

    private final LoyaltyClient loyaltyClient;
    private final PaymentClient paymentClient;
    private final ReservationClient reservationClient;
    private final QueueProcessor queueProcessor;

    // ==========================================
    // 1. عمليات القراءة (GET) - تستخدم Fallbacks
    // ==========================================
    public List<HotelResponse> getAllHotels() {
        return reservationClient.getAllHotels();
    }

    public List<FullReservationResponse> getUserReservations(String username) {
        return reservationClient.getUserReservations(username);
    }

    public FullReservationResponse getReservation(UUID reservationUid) {
        return reservationClient.getReservation(reservationUid);
    }

    public Object getMe(String username) {
        LoyaltyInfoResponse loyalty = loyaltyClient.getLoyaltyInfo(username);
        List<FullReservationResponse> reservations = reservationClient.getUserReservations(username);
        return new Object() {
            public final LoyaltyInfoResponse loyaltyInfo = loyalty;
            public final List<FullReservationResponse> reservationsList = reservations;
        };
    }

    // ==========================================
    // 2. العمليات المعقدة (Write) - Rollback + Queue
    // ==========================================
    public CreateReservationResponse bookHotel(CreateReservationRequest request) {
        UUID paymentUid = null;
        UUID reservationUid = null;

        try {
            // 1. جلب الفندق
            HotelResponse hotel = reservationClient.getHotel(request.getHotelUid());
            if (hotel == null) throw new RuntimeException("Hotel not found or Reservation Service down");

            // 2. جلب الولاء
            LoyaltyInfoResponse loyalty = loyaltyClient.getLoyaltyInfo(request.getUsername());
            int discount = loyalty != null ? loyalty.getDiscount() : 0;
            int finalPrice = hotel.getPrice() - (hotel.getPrice() * discount / 100);

            // 3. إنشاء الدفعة
            PaymentResponse payment = paymentClient.createPayment(finalPrice);
            if (payment == null || payment.getPaymentUid() == null) throw new RuntimeException("Payment failed");
            paymentUid = payment.getPaymentUid();

            // 4. إنشاء الحجز
            InternalReservationRequest resReq = new InternalReservationRequest(
                    request.getUsername(), paymentUid, request.getHotelUid(), request.getStartDate(), request.getEndDate()
            );
            InternalReservationResponse reservation = reservationClient.createReservation(resReq);
            if (reservation == null || reservation.getReservationUid() == null) throw new RuntimeException("Reservation failed");
            reservationUid = reservation.getReservationUid();

            // 5. زيادة عدّاد الولاء
            loyaltyClient.increment(request.getUsername());

            // إذا نجحت كل الخطوات
            return new CreateReservationResponse(
                    reservationUid, hotel.getHotelUid(), request.getStartDate(), request.getEndDate(),
                    discount, "PAID", finalPrice
            );

        } catch (Exception e) {
            // ⚠️ فشلت إحدى الخطوات! يجب التراجع عن ما تم إنجازه
            System.out.println("Operation failed, rolling back... Cause: " + e.getMessage());

            if (reservationUid != null) {
                try { reservationClient.cancelReservation(reservationUid); } catch (Exception ex) { System.out.println("Rollback reservation failed"); }
            }
            if (paymentUid != null) {
                try { paymentClient.cancelPayment(paymentUid); } catch (Exception ex) { System.out.println("Rollback payment failed"); }
            }

            // وضع المهمة في الطابور لإعادة تنفيذ العملية بالكامل لاحقاً
            queueProcessor.addTask(() -> bookHotel(request));

            // إرجاع نجاح وهمي للمستخدم (كما يطلب المتطلب)
            return new CreateReservationResponse(
                    UUID.randomUUID(), request.getHotelUid(), request.getStartDate(), request.getEndDate(),
                    0, "PAID", 0
            );
        }
    }

    public void cancelReservation(UUID reservationUid) {
        try {
            FullReservationResponse reservation = reservationClient.getReservation(reservationUid);
            if (reservation == null) throw new RuntimeException("Reservation not found");

            // 1. إلغاء الدفعة
            paymentClient.cancelPayment(reservation.getPaymentUid());

            // 2. إنقاص الولاء وإلغاء الحجز
            loyaltyClient.decrement(reservation.getUsername());
            reservationClient.cancelReservation(reservationUid);

        } catch (Exception e) {
            // فشل إلغاء الحجز! نضع المهمة في الطابور ونرجع نجاح وهمي
            System.out.println("Cancel failed, queuing... Cause: " + e.getMessage());
            queueProcessor.addTask(() -> cancelReservation(reservationUid));
            // الـ Controller سيرجع 204 No Content تلقائياً
        }
    }
}