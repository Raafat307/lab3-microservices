package ru.bmstu.rsoi.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InternalReservationResponse {
    private UUID reservationUid;
    private UUID hotelUid;
    private String status;
}