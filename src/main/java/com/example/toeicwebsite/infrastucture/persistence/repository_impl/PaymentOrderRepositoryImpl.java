package com.example.toeicwebsite.infrastucture.persistence.repository_impl;

import com.example.toeicwebsite.domain.payment_order.model.PaymentOrder;
import com.example.toeicwebsite.domain.payment_order.repository.PaymentOrderRepository;
import com.example.toeicwebsite.infrastucture.persistence.entity.PaymentOrderEntity;
import com.example.toeicwebsite.infrastucture.persistence.jpa_repository.JpaPaymentOrderRepository;
import com.example.toeicwebsite.infrastucture.persistence.mapper.PaymentOrderEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PaymentOrderRepositoryImpl implements PaymentOrderRepository {

    private final JpaPaymentOrderRepository jpaRepository;
    private final PaymentOrderEntityMapper mapper;

    @Override
    public PaymentOrder save(PaymentOrder paymentOrder) {
        PaymentOrderEntity mapped = mapper.toEntity(paymentOrder);
        PaymentOrderEntity saved = jpaRepository.save(mapped);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<PaymentOrder> findByOrderCode(String orderCode) {
        return jpaRepository.findByOrderCode(orderCode).map(mapper::toDomain);
    }

    @Override
    public Optional<PaymentOrder> findByRegistrationId(com.example.toeicwebsite.domain.exam_registration.model.ExamRegistrationId id) {
        return jpaRepository.findByExamRegistrationId(id.value()).map(mapper::toDomain);
    }
}
