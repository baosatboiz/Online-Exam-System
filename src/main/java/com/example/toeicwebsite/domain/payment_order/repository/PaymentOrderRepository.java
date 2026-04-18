package com.example.toeicwebsite.domain.payment_order.repository;

import com.example.toeicwebsite.domain.payment_order.model.PaymentOrder;

public interface PaymentOrderRepository {
    PaymentOrder save(PaymentOrder paymentOrder);
    java.util.Optional<PaymentOrder> findByOrderCode(String orderCode);
    java.util.Optional<PaymentOrder> findByRegistrationId(com.example.toeicwebsite.domain.exam_registration.model.ExamRegistrationId id);
}
