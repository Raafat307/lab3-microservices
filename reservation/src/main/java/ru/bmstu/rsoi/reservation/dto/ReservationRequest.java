package ru.bmstu.rsoi.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequest {
    private String username;
    private UUID paymentUid;
    private UUID hotelUid;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
}