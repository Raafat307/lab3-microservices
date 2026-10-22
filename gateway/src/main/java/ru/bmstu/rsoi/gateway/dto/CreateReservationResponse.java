package ru.bmstu.rsoi.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReservationResponse {
    private UUID reservationUid;
    private UUID hotelUid;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private Integer discount;
    private String status;
    private Integer paymentPrice;
}