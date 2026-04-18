package com.example.toeicwebsite.application.service;

import com.example.toeicwebsite.application.port.PaymentPort;
import com.example.toeicwebsite.application.result.GetPaymentInfoResult;
import com.example.toeicwebsite.application.usecase.GetPaymentInfo;
import com.example.toeicwebsite.domain.exam_registration.model.ExamRegistration;
import com.example.toeicwebsite.domain.exam_registration.repository.ExamRegistrationRepository;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamScheduleId;
import com.example.toeicwebsite.domain.exception.DomainNotFoundException;
import com.example.toeicwebsite.domain.payment_order.model.PaymentOrder;
import com.example.toeicwebsite.domain.payment_order.repository.PaymentOrderRepository;
import com.example.toeicwebsite.domain.user.model.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetPaymentInfoImpl implements GetPaymentInfo {
    
    private final ExamRegistrationRepository registrationRepository;
    private final PaymentOrderRepository paymentOrderRepository;
    private final PaymentPort port;

    @Transactional(readOnly = true)
    @Override
    public GetPaymentInfoResult getPaymentInfo(UserId userId, ExamScheduleId examScheduleId) {
        ExamRegistration registration = registrationRepository
                .findByUserIdAndScheduleId(userId, examScheduleId)
                .orElseThrow(() -> new DomainNotFoundException("Registration not found"));
        
        PaymentOrder paymentOrder = paymentOrderRepository
                .findByRegistrationId(registration.getExamRegistrationId())
                .orElseThrow(() -> new DomainNotFoundException("Payment Order not found"));

        return new GetPaymentInfoResult(
                registration.getRegistrationStatus().name(),
                paymentOrder.getOrderCode(),
                paymentOrder.getPrice().getAmount(),
                port.generateQrUrl(paymentOrder.getOrderCode(), paymentOrder.getPrice()),
                port.generatePaymentContent(paymentOrder.getOrderCode(), paymentOrder.getPrice()),
                registration.getExpiredAt(),
                paymentOrder.getPaymentStatus().name()
        );
    }
}
