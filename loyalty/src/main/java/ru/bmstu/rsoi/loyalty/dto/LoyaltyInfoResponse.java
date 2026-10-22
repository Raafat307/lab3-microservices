package ru.bmstu.rsoi.loyalty.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoyaltyInfoResponse {
    private String status;
    private Integer discount;
    private Integer reservationCount;
}