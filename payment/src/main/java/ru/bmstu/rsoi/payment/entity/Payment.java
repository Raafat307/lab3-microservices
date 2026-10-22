package ru.bmstu.rsoi.payment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "payment_uid", nullable = false, unique = true)
    private UUID paymentUid;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(nullable = false)
    private Integer price;
}