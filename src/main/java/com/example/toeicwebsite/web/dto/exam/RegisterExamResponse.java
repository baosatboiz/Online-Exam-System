package com.example.toeicwebsite.web.dto.exam;

public record RegisterExamResponse(
        String registrationId,
        PaymentInfoResponse paymentInfo
) {
    public record PaymentInfoResponse(
            String orderCode,
            String amount,
            String qrCodeUrl,
            String paymentContent,
            java.time.Instant expiredAt,
            String status
    ) {}
}
