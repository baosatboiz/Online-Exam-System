package com.example.toeicwebsite.application.result;

import java.math.BigDecimal;
import java.time.Instant;

public record GetPaymentInfoResult(
        String registrationStatus,
        String orderCode,
        BigDecimal amount,
        String qrCodeUrl,
        String paymentContent,
        Instant expiredAt,
        String paymentStatus
) {
}
