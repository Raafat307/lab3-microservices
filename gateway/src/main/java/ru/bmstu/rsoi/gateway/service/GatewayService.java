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

    // السيناريو الكامل: حجز فندق
    public CreateReservationResponse bookHotel(CreateReservationRequest request) {

        // 1. جلب معلومات الولاء
        LoyaltyInfoResponse loyalty = loyaltyClient.getLoyaltyInfo(request.getUsername());
        int discount = loyalty.getDiscount();

        // 2. جلب الفندق لمعرفة السعر
        HotelResponse hotel = reservationClient.getHotel(request.getHotelUid());
        int originalPrice = hotel.getPrice();

        // 3. حساب السعر بعد الخصم
        int finalPrice = originalPrice - (originalPrice * discount / 100);

        // 4. إنشاء الدفعة
        PaymentResponse payment = paymentClient.createPayment(finalPrice);

        // 5. إنشاء الحجز
        InternalReservationRequest resReq = new InternalReservationRequest(
                request.getUsername(),
                payment.getPaymentUid(),
                request.getHotelUid(),
                request.getStartDate(),
                request.getEndDate()
        );
        InternalReservationResponse reservation = reservationClient.createReservation(resReq);

        // 6. زيادة عدّاد الولاء
        loyaltyClient.increment(request.getUsername());

        // 7. إعادة الرد الكامل
        return new CreateReservationResponse(
                reservation.getReservationUid(),
                reservation.getHotelUid(),
                request.getStartDate(),
                request.getEndDate(),
                discount,
                reservation.getStatus(),
                finalPrice
        );
    }

    // جلب حجوزات المستخدم
    public List<FullReservationResponse> getUserReservations(String username) {
        return reservationClient.getUserReservations(username);
    }

    // جلب حجز محدد
    public FullReservationResponse getReservation(UUID reservationUid) {
        return reservationClient.getReservation(reservationUid);
    }

    // إلغاء حجز
    public void cancelReservation(UUID reservationUid) {
        // 1. جلب الحجز لمعرفة paymentUid و username
        FullReservationResponse reservation = reservationClient.getReservation(reservationUid);

        // 2. إلغاء الدفعة
        paymentClient.cancelPayment(reservation.getPaymentUid());

        // 3. إلغاء الحجز
        reservationClient.cancelReservation(reservationUid);

        // 4. إنقاص عدّاد الولاء
        loyaltyClient.decrement(reservation.getUsername());
    }

    // قائمة الفنادق
    public List<HotelResponse> getAllHotels() {
        return reservationClient.getAllHotels();
    }

    // معلومات المستخدم (ولاء + حجوزات)
    public Object getMe(String username) {
        LoyaltyInfoResponse loyalty = loyaltyClient.getLoyaltyInfo(username);
        List<FullReservationResponse> reservations = reservationClient.getUserReservations(username);

        return new Object() {
            public final LoyaltyInfoResponse loyaltyInfo = loyalty;
            public final List<FullReservationResponse> reservationsList = reservations;
        };
    }
}