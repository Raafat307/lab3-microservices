package ru.bmstu.rsoi.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReservationResponse {
    private UUID reservationUid;
    private UUID hotelUid;
    private String status;
}