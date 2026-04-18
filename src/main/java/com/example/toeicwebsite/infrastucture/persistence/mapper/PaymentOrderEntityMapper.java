package com.example.toeicwebsite.infrastucture.persistence.mapper;

import com.example.toeicwebsite.domain.exam_registration.model.ExamRegistrationId;
import com.example.toeicwebsite.domain.payment_order.model.PaymentOrder;
import com.example.toeicwebsite.domain.payment_order.model.PaymentOrderId;
import com.example.toeicwebsite.domain.payment_order.model.PaymentStatus;
import com.example.toeicwebsite.domain.shared.Money;
import com.example.toeicwebsite.infrastucture.persistence.entity.PaymentOrderEntity;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface PaymentOrderEntityMapper {


    @Mapping(target = "paymentOrderId",     source = "paymentOrderId")
    @Mapping(target = "examRegistrationId", source = "examRegistrationId")
    @Mapping(target = "price",              source = "price")
    @Mapping(target = "paymentStatus",      source = "paymentStatus")
    @Mapping(target = "bankTransactionId",  source = "bankTransactionId")
    @Mapping(target = "paidAt",             source = "paidAt")
    PaymentOrder toDomain(PaymentOrderEntity entity);

    @Mapping(target = "paymentOrderId",     source = "paymentOrderId")
    @Mapping(target = "examRegistrationId", source = "examRegistrationId")
    @Mapping(target = "price",              source = "price")
    @Mapping(target = "paymentStatus",      source = "paymentStatus")
    PaymentOrderEntity toEntity(PaymentOrder domain);


    default PaymentOrderId mapToPaymentOrderId(UUID uuid) {
        return uuid != null ? new PaymentOrderId(uuid) : null;
    }

    default UUID mapFromPaymentOrderId(PaymentOrderId id) {
        return id != null ? id.value() : null;
    }

    default ExamRegistrationId mapToExamRegistrationId(UUID uuid) {
        return uuid != null ? new ExamRegistrationId(uuid) : null;
    }

    default UUID mapFromExamRegistrationId(ExamRegistrationId id) {
        return id != null ? id.value() : null;
    }

    default Money map(BigDecimal amount) {
        return amount != null ? new Money(amount, "VND") : null;
    }

    default BigDecimal map(Money money) {
        return money != null ? money.getAmount() : null;
    }

    default PaymentStatus map(String status) {
        return status != null ? PaymentStatus.valueOf(status) : null;
    }

    default String map(PaymentStatus status) {
        return status != null ? status.name() : null;
    }
}
