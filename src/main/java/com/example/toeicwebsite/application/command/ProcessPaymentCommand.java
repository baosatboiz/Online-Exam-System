package com.example.toeicwebsite.application.command;

import com.example.toeicwebsite.domain.shared.Money;

public record ProcessPaymentCommand(
        String orderCode,
        Money transferAmount,
        String bankTransactionId,
        String paymentGateway
) {}