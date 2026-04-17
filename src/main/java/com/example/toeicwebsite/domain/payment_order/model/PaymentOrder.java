package com.example.toeicwebsite.domain.payment_order.model;

import com.example.toeicwebsite.domain.exam_registration.model.ExamRegistrationId;
import com.example.toeicwebsite.domain.exception.BusinessRuleException;
import com.example.toeicwebsite.domain.shared.Money;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class PaymentOrder {
    private final PaymentOrderId paymentOrderId;
    private final ExamRegistrationId examRegistrationId;
    private final Money price;
    private PaymentStatus paymentStatus;
    private final String orderCode;
    private String bankTransactionId;
    private Instant paidAt;
    private final Instant createdAt;

    private PaymentOrder(PaymentOrderId paymentOrderId, ExamRegistrationId examRegistrationId,
                         Money price, PaymentStatus paymentStatus, String orderCode, Instant createdAt) {
        this.paymentOrderId = paymentOrderId;
        this.examRegistrationId = examRegistrationId;
        this.price = price;
        this.paymentStatus = paymentStatus;
        this.orderCode = orderCode;
        this.createdAt = createdAt;
    }

    public static PaymentOrder create(ExamRegistrationId regId, Money amount) {
        if (regId == null) throw new BusinessRuleException("Registration ID is required");
        if (amount.isZeroOrLess()) throw new BusinessRuleException("Amount must be positive");

        String generatedCode = "TOEIC-" + UUID.randomUUID();

        return new PaymentOrder(
                PaymentOrderId.newId(),
                regId,
                amount,
                PaymentStatus.PENDING,
                generatedCode,
                Instant.now()
        );
    }

    public void markAsPaid(String bankTxId, Instant now) {
        if (this.paymentStatus != PaymentStatus.PENDING) {
            throw new BusinessRuleException("Only pending orders can be marked as paid. Current status: " + this.paymentStatus);
        }

        if (bankTxId == null || bankTxId.isBlank()) {
            throw new BusinessRuleException("Bank transaction ID is required for payment confirmation");
        }

        this.paymentStatus = PaymentStatus.PAID;
        this.bankTransactionId = bankTxId;
        this.paidAt = now;
    }

    public void cancel() {
        if (this.paymentStatus == PaymentStatus.PENDING) {
            this.paymentStatus = PaymentStatus.CANCELLED;
        }
    }

    public boolean isPaid() {
        return this.paymentStatus == PaymentStatus.PAID;
    }
}