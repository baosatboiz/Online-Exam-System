package com.example.toeicwebsite.application.usecase;

import com.example.toeicwebsite.application.command.ProcessPaymentCommand;
import com.example.toeicwebsite.application.result.ProcessPaymentResult;

public interface ProcessPayment {
    ProcessPaymentResult handle(ProcessPaymentCommand command);
}
