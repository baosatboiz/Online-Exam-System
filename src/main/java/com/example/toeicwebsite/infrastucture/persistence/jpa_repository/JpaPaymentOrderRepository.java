package com.example.toeicwebsite.infrastucture.persistence.jpa_repository;

import com.example.toeicwebsite.infrastucture.persistence.entity.PaymentOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaPaymentOrderRepository extends JpaRepository<PaymentOrderEntity, Long> {
    Optional<PaymentOrderEntity> findByOrderCode(String orderCode);
    Optional<PaymentOrderEntity> findByExamRegistrationId(UUID examRegistrationId);
    Optional<PaymentOrderEntity> findByBusinessId(UUID businessId);
}
