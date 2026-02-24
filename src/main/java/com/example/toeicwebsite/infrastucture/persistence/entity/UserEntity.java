package com.example.toeicwebsite.infrastucture.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "user_entity")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "bussiness_id")
    private UUID bussinessId;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @JdbcTypeCode(SqlTypes.ARRAY)
    private List<String> role;
}
