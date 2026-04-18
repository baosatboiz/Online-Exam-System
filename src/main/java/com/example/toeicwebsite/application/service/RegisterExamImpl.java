package com.example.toeicwebsite.application.service;

import com.example.toeicwebsite.application.command.RegisterExamCommand;
import com.example.toeicwebsite.application.port.PaymentPort;
import com.example.toeicwebsite.application.result.RegisterExamResult;
import com.example.toeicwebsite.application.usecase.RegisterExam;
import com.example.toeicwebsite.domain.exam_registration.model.ExamRegistration;
import com.example.toeicwebsite.domain.exam_registration.repository.ExamRegistrationRepository;
import com.example.toeicwebsite.domain.exam_registration.service.RegistrationPolicy;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamSchedule;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamScheduleId;
import com.example.toeicwebsite.domain.exam_schedule.repository.ExamScheduleRepository;
import com.example.toeicwebsite.domain.exception.DomainNotFoundException;
import com.example.toeicwebsite.domain.payment_order.model.PaymentOrder;
import com.example.toeicwebsite.domain.payment_order.repository.PaymentOrderRepository;
import com.example.toeicwebsite.domain.user.model.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class RegisterExamImpl implements RegisterExam {
    private final RegistrationPolicy registrationPolicy;
    private final ExamScheduleRepository examScheduleRepository;
    private final ExamRegistrationRepository examRegistrationRepository;
    private final PaymentOrderRepository paymentOrderRepository;
    private final PaymentPort port;
    @Transactional
    @Override
    public RegisterExamResult handle(RegisterExamCommand command) {
        UserId userId = command.userId();
        ExamScheduleId examScheduleId = command.examScheduleId();
        ExamSchedule examSchedule = examScheduleRepository.findByBusinessId(examScheduleId.value()).orElseThrow(()->new DomainNotFoundException("This id doesn't belong to any schedule"));
        registrationPolicy.validateRegistration(examSchedule,userId);
        ExamRegistration examRegistration = ExamRegistration.createPending(examScheduleId,userId);
        ExamRegistration savedExamRegistration = examRegistrationRepository.save(examRegistration);
        PaymentOrder paymentOrder = PaymentOrder.create(savedExamRegistration.getExamRegistrationId(),examSchedule.getPrice());
        paymentOrderRepository.save(paymentOrder);
        return new RegisterExamResult(
                examRegistration.getExamRegistrationId(),
                new RegisterExamResult.PaymentOrderResult(
                        paymentOrder.getOrderCode(),
                        paymentOrder.getPrice(),
                        port.generateQrUrl(paymentOrder.getOrderCode(),paymentOrder.getPrice()),
                        port.generatePaymentContent(paymentOrder.getOrderCode(), paymentOrder.getPrice()),
                        examRegistration.getExpiredAt(),
                        paymentOrder.getPaymentStatus().name()
                )
        );
    }
}
