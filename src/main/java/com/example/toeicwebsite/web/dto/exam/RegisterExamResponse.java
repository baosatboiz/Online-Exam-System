package com.example.toeicwebsite.web.dto.exam;

import java.time.Instant;

public record RegisterExamResponse(
        String registrationId,
        PaymentInfoResponse paymentInfo
) {
    public record PaymentInfoResponse(
            String orderCode,
            String amount,
            String qrCodeUrl,
            String paymentContent,
            Instant expiredAt,
            String status
    ) {}
}
