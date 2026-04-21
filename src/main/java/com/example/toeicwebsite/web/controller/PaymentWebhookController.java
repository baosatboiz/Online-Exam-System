package com.example.toeicwebsite.web.controller;

import com.example.toeicwebsite.application.command.ProcessPaymentCommand;
import com.example.toeicwebsite.application.usecase.ProcessPayment;
import com.example.toeicwebsite.domain.shared.Money;
import com.example.toeicwebsite.web.dto.payment.SepayWebhookRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
@Slf4j
public class PaymentWebhookController {

    private final ProcessPayment processPayment;

    @org.springframework.beans.factory.annotation.Value("${payment.sepay.webhook-token}")
    private String webhookToken;

    @PostMapping("/webhook/sepay")
    public ResponseEntity<String> handleSepayWebhook(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody SepayWebhookRequest request) {
        
        log.info("Received Sepay webhook: {}", request);

        // Security check
        if (authHeader == null || !authHeader.equals("Apikey " + webhookToken)) {
            log.warn("Unauthorized webhook attempt with header: {}", authHeader);
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String content = request.content();
        if (content == null) {
            return ResponseEntity.badRequest().body("Missing transaction content");
        }

        Pattern pattern = Pattern.compile("TOEIC-[a-fA-F0-9\\-]+");
        Matcher matcher = pattern.matcher(content);

        String orderCode = null;
        if (matcher.find()) {
            orderCode = matcher.group();
        }

        if (orderCode == null) {
            log.warn("Cannot find valid orderCode in content: {}", content);
            return ResponseEntity.ok("Received but ignored (no order code)");
        }

        ProcessPaymentCommand command = new ProcessPaymentCommand(
                orderCode,
                new Money(request.transferAmount(), "VND"),
                request.referenceCode() != null ? request.referenceCode() : request.code(),
                "SEPAY"
        );

        try {
            processPayment.handle(command);
            return ResponseEntity.ok("Processed successfully");
        } catch (Exception e) {
            log.error("Error processing payment webhook", e);
            return ResponseEntity.status(500).body("Error processing payment: " + e.getMessage());
        }
    }
}
