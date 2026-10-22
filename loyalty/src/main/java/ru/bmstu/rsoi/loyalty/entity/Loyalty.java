package ru.bmstu.rsoi.loyalty.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "loyalty")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Loyalty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 80)
    private String username;

    @Column(name = "reservation_count", nullable = false)
    private Integer reservationCount;

    @Column(nullable = false, length = 80)
    private String status;

    @Column(nullable = false)
    private Integer discount;
}