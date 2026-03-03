package com.example.toeicwebsite.infrastucture.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(
        name = "user_provider",
        uniqueConstraints = @UniqueConstraint(columnNames = {"provider", "provider_id"})
)
public class UserProviderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_business_id", nullable = false)
    private UUID userBusinessId;

    @Column(nullable = false)
    private String provider;

    @Column(name = "provider_id")
    private String providerId;
}
