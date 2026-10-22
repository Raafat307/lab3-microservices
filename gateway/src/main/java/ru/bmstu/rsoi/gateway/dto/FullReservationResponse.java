package ru.bmstu.rsoi.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FullReservationResponse {
    private UUID reservationUid;
    private String username;
    private UUID paymentUid;
    private UUID hotelUid;
    private String status;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
}