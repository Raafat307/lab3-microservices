package ru.bmstu.rsoi.gateway.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.bmstu.rsoi.gateway.dto.*;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReservationClient {

    @Value("${services.reservation.url}")
    private String reservationUrl;

    private final WebClient.Builder webClientBuilder;

    public List<HotelResponse> getAllHotels() {
        return webClientBuilder.build()
                .get()
                .uri(reservationUrl + "/api/v1/hotels")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<HotelResponse>>() {})
                .block();
    }

    public HotelResponse getHotel(UUID hotelUid) {
        return webClientBuilder.build()
                .get()
                .uri(reservationUrl + "/api/v1/hotels/{uid}", hotelUid)
                .retrieve()
                .bodyToMono(HotelResponse.class)
                .block();
    }

    public List<FullReservationResponse> getUserReservations(String username) {
        return webClientBuilder.build()
                .get()
                .uri(reservationUrl + "/api/v1/reservations?username={u}", username)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<FullReservationResponse>>() {})
                .block();
    }

    public FullReservationResponse getReservation(UUID reservationUid) {
        return webClientBuilder.build()
                .get()
                .uri(reservationUrl + "/api/v1/reservations/{uid}", reservationUid)
                .retrieve()
                .bodyToMono(FullReservationResponse.class)
                .block();
    }

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