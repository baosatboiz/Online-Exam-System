package com.example.toeicwebsite.web.dto.payment;

import java.math.BigDecimal;

public record SepayWebhookRequest(
        Long id,
        String gateway,
        String transactionDate,
        String accountNumber,
        String subAccount,
        String transferType,
        BigDecimal transferAmount,
        BigDecimal accumulated,
        String code,
        String content,
        String referenceCode,
        String description
) {
}
