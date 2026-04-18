package com.example.toeicwebsite.application.result;

import java.time.Instant;

public record GetPaymentInfoResult(
        String registrationStatus,
        String orderCode,
        java.math.BigDecimal amount,
        String qrCodeUrl,
        String paymentContent,
        Instant expiredAt,
        String paymentStatus
) {
}
