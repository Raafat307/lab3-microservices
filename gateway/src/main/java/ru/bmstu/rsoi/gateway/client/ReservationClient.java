package ru.bmstu.rsoi.gateway.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.bmstu.rsoi.gateway.dto.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReservationClient {

    @Value("${services.reservation.url}")
    private String reservationUrl;

    private final WebClient.Builder webClientBuilder;

    @CircuitBreaker(name = "reservationService", fallbackMethod = "fallbackGetAllHotels")
    public List<HotelResponse> getAllHotels() {
        return webClientBuilder.build()
                .get()
                .uri(reservationUrl + "/api/v1/hotels")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<HotelResponse>>() {})
                .block();
    }

    public List<HotelResponse> fallbackGetAllHotels(Throwable t) {
        return Collections.emptyList();
    }

    @CircuitBreaker(name = "reservationService", fallbackMethod = "fallbackGetHotel")
    public HotelResponse getHotel(UUID hotelUid) {
        return webClientBuilder.build()
                .get()
                .uri(reservationUrl + "/api/v1/hotels/{uid}", hotelUid)
                .retrieve()
                .bodyToMono(HotelResponse.class)
                .block();
    }

    public HotelResponse fallbackGetHotel(UUID hotelUid, Throwable t) {
        return null; // أو يمكن إرجاع كائن فارغ
    }

    @CircuitBreaker(name = "reservationService", fallbackMethod = "fallbackGetUserReservations")
    public List<FullReservationResponse> getUserReservations(String username) {
        return webClientBuilder.build()
                .get()
                .uri(reservationUrl + "/api/v1/reservations?username={u}", username)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<FullReservationResponse>>() {})
                .block();
    }

    public List<FullReservationResponse> fallbackGetUserReservations(String username, Throwable t) {
        return Collections.emptyList();
    }

    @CircuitBreaker(name = "reservationService", fallbackMethod = "fallbackGetReservation")
    public FullReservationResponse getReservation(UUID reservationUid) {
        return webClientBuilder.build()
                .get()
                .uri(reservationUrl + "/api/v1/reservations/{uid}", reservationUid)
                .retrieve()
                .bodyToMono(FullReservationResponse.class)
                .block();
    }

    public FullReservationResponse fallbackGetReservation(UUID reservationUid, Throwable t) {
        return null;
    }

    // العمليات التي تغير الحالة (Write) - لا نطبق عليها CircuitBreaker هنا
    public InternalReservationResponse createReservation(InternalReservationRequest req) {
        return webClientBuilder.build()
                .post()
                .uri(reservationUrl + "/api/v1/reservations")
                .bodyValue(req)
                .retrieve()
                .bodyToMono(InternalReservationResponse.class)
                .block();
    }

    public void cancelReservation(UUID reservationUid) {
        webClientBuilder.build()
                .delete()
                .uri(reservationUrl + "/api/v1/reservations/{uid}", reservationUid)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}