package ru.bmstu.rsoi.gateway.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.bmstu.rsoi.gateway.dto.LoyaltyInfoResponse;
import ru.bmstu.rsoi.gateway.dto.LoyaltyRequest;

@Component
@RequiredArgsConstructor
public class LoyaltyClient {

    @Value("${services.loyalty.url}")
    private String loyaltyUrl;

    private final WebClient.Builder webClientBuilder;

    public LoyaltyInfoResponse getLoyaltyInfo(String username) {
        return webClientBuilder.build()
                .get()
                .uri(loyaltyUrl + "/api/v1/loyalty?username={u}", username)
                .retrieve()
                .bodyToMono(LoyaltyInfoResponse.class)
                .block();
    }

    public LoyaltyInfoResponse increment(String username) {
        LoyaltyRequest req = new LoyaltyRequest(username);
        return webClientBuilder.build()
                .post()
                .uri(loyaltyUrl + "/api/v1/loyalty/increment")
                .bodyValue(req)
                .retrieve()
                .bodyToMono(LoyaltyInfoResponse.class)
                .block();
    }

    public LoyaltyInfoResponse decrement(String username) {
        LoyaltyRequest req = new LoyaltyRequest(username);
        return webClientBuilder.build()
                .post()
                .uri(loyaltyUrl + "/api/v1/loyalty/decrement")
                .bodyValue(req)
                .retrieve()
                .bodyToMono(LoyaltyInfoResponse.class)
                .block();
    }
}