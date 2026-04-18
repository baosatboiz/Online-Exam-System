package com.example.toeicwebsite.application.service;

import com.example.toeicwebsite.application.command.ProcessPaymentCommand;
import com.example.toeicwebsite.application.result.ProcessPaymentResult;
import com.example.toeicwebsite.application.usecase.ProcessPayment;
import com.example.toeicwebsite.domain.exam_registration.model.ExamRegistration;
import com.example.toeicwebsite.domain.exam_registration.repository.ExamRegistrationRepository;
import com.example.toeicwebsite.domain.exception.BusinessRuleException;
import com.example.toeicwebsite.domain.exception.DomainNotFoundException;
import com.example.toeicwebsite.domain.payment_order.model.PaymentOrder;
import com.example.toeicwebsite.domain.payment_order.repository.PaymentOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ProcessPaymentImpl implements ProcessPayment {
    private final PaymentOrderRepository paymentOrderRepository;
    private final ExamRegistrationRepository examRegistrationRepository;

    @Transactional
    @Override
    public ProcessPaymentResult handle(ProcessPaymentCommand command) {
        PaymentOrder paymentOrder = paymentOrderRepository.findByOrderCode(command.orderCode())
                .orElseThrow(() -> new DomainNotFoundException("PaymentOrder not found for code: " + command.orderCode()));

        if (command.transferAmount().getAmount().compareTo(paymentOrder.getPrice().getAmount()) < 0) {
            throw new BusinessRuleException("Transfer amount is less than required price");
        }

        ExamRegistration registration = examRegistrationRepository.findById(paymentOrder.getExamRegistrationId())
                .orElseThrow(() -> new DomainNotFoundException("Registration not found associated with payment order"));

        Instant now = Instant.now();
        paymentOrder.markAsPaid(command.bankTransactionId(), now);
        registration.confirm();

        paymentOrderRepository.save(paymentOrder);
        examRegistrationRepository.save(registration);

        return ProcessPaymentResult.ok();
    }
}
