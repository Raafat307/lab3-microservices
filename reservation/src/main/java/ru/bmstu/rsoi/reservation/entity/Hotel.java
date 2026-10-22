package ru.bmstu.rsoi.reservation.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "hotels")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "hotel_uid", nullable = false, unique = true)
    private UUID hotelUid;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 80)
    private String country;

    @Column(nullable = false, length = 80)
    private String city;

    @Column(nullable = false, length = 255)
    private String address;

    private Integer stars;

    @Column(nullable = false)
    private Integer price;
}