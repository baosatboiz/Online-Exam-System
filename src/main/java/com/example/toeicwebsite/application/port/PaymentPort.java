package com.example.toeicwebsite.application.port;

import com.example.toeicwebsite.domain.shared.Money;

public interface PaymentPort {
    String generateQrUrl(String orderCode, Money amout);
    String generatePaymentContent(String orderCode,Money amount);

}
