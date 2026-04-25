package com.example.toeicwebsite.application.result;

import com.example.toeicwebsite.domain.exam_registration.model.ExamRegistrationId;
import com.example.toeicwebsite.domain.shared.Money;

import java.time.Instant;

public record RegisterExamResult(
        ExamRegistrationId registrationId,
        PaymentOrderResult paymentOrderResult
) {
    public record PaymentOrderResult(
            String orderCode,
            Money amount,
            String qrCodeUrl,
            String paymentContent,
            Instant expiredAt,
            String status
    ){}
}