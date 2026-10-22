package ru.bmstu.rsoi.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelResponse {
    private UUID hotelUid;
    private String name;
    private String country;
    private String city;
    private String address;
    private Integer stars;
    private Integer price;
}