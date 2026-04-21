package com.example.toeicwebsite.infrastucture.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payment_order")
@Getter
@Setter
public class PaymentOrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "business_id", nullable = false, unique = true)
    private UUID businessId;

    @Column(name = "exam_registration_id", nullable = false)
    private UUID examRegistrationId;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "payment_status", nullable = false)
    private String paymentStatus;

    @Column(name = "order_code", nullable = false, unique = true)
    private String orderCode;

    @Column(name = "bank_transaction_id")
    private String bankTransactionId;

    @Column(name = "paid_at")
    private Instant paidAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
