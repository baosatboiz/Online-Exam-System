package com.example.toeicwebsite.application.result;

public record ProcessPaymentResult(
        boolean success,
        String message
) {
    public static ProcessPaymentResult ok() {
        return new ProcessPaymentResult(true, "Payment processed successfully");
    }

    public static ProcessPaymentResult error(String message) {
        return new ProcessPaymentResult(false, message);
    }
}