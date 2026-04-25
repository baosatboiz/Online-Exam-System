package com.example.toeicwebsite.web.mapper.register_exam;

import com.example.toeicwebsite.application.result.RegisterExamResult;
import com.example.toeicwebsite.web.dto.exam.RegisterExamResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ExamRegistrationMapper {
    @Mapping(target = "registrationId", expression = "java(result.registrationId().value().toString())")
    @Mapping(target = "paymentInfo", source = "paymentOrderResult")
    RegisterExamResponse toResponse(RegisterExamResult result);

    @Mapping(target = "amount", expression = "java(payment.amount().getAmount().toPlainString())")
    RegisterExamResponse.PaymentInfoResponse toPaymentInfoResponse(RegisterExamResult.PaymentOrderResult payment);
}
