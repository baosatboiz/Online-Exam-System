package com.example.toeicwebsite.infrastucture.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "exam_registration")
@Getter
@Setter
public class ExamRegistrationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "business_id", nullable = false, unique = true)
    private UUID businessId;

    @Column(name = "exam_schedule_id", nullable = false)
    private UUID examScheduleId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "registration_status", nullable = false)
    private String registrationStatus;

    @Column(name = "create_at", nullable = false)
    private Instant createAt;

    @Column(name = "confirm_at")
    private Instant confirmAt;

    @Column(name = "expired_at")
    private Instant expiredAt;
}
