package com.example.toeicwebsite.infrastucture.payment;

import com.example.toeicwebsite.application.port.PaymentPort;
import com.example.toeicwebsite.domain.shared.Money;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class SepayPaymentAdapter implements PaymentPort {

    @Value("${payment.sepay.bank-bin}")
    private String bankBin;

    @Value("${payment.sepay.account-number}")
    private String accountNo;

    @Value("${payment.sepay.account-name}")
    private String accountName;

    @Override
    public String generateQrUrl(String orderCode, Money amount) {
        String content = URLEncoder.encode(generatePaymentContent(orderCode, amount), StandardCharsets.UTF_8);
        String encodedName = URLEncoder.encode(accountName, StandardCharsets.UTF_8);

        return String.format(
                "https://img.vietqr.io/image/%s-%s-compact.png?amount=%d&addInfo=%s&accountName=%s",
                bankBin,
                accountNo,
                amount.getAmount().longValue(),
                content,
                encodedName
        );
    }

    @Override
    public String generatePaymentContent(String orderCode, Money amount) {
        return orderCode;
    }
}
