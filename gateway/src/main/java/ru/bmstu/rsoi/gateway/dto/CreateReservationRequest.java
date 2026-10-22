package ru.bmstu.rsoi.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReservationRequest {
    private String username;
    private UUID hotelUid;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
}