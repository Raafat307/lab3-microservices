package ru.bmstu.rsoi.gateway.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.bmstu.rsoi.gateway.dto.PaymentRequest;
import ru.bmstu.rsoi.gateway.dto.PaymentResponse;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaymentClient {

    @Value("${services.payment.url}")
    private String paymentUrl;

    private final WebClient.Builder webClientBuilder;

    public PaymentResponse createPayment(Integer price) {
        PaymentRequest req = new PaymentRequest(price);
        return webClientBuilder.build()
                .post()
                .uri(paymentUrl + "/api/v1/payment")
                .bodyValue(req)
                .retrieve()
                .bodyToMono(PaymentResponse.class)
                .block();
    }

    public PaymentResponse cancelPayment(UUID paymentUid) {
        return webClientBuilder.build()
                .post()
                .uri(paymentUrl + "/api/v1/payment/{uid}/cancel", paymentUid)
                .retrieve()
                .bodyToMono(PaymentResponse.class)
                .block();
    }
}