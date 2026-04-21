package com.example.toeicwebsite.web.controller;

import com.example.toeicwebsite.application.command.RegisterExamCommand;
import com.example.toeicwebsite.application.result.GetPaymentInfoResult;
import com.example.toeicwebsite.application.result.RegisterExamResult;
import com.example.toeicwebsite.application.usecase.GetPaymentInfo;
import com.example.toeicwebsite.application.usecase.RegisterExam;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamScheduleId;
import com.example.toeicwebsite.infrastucture.security.config.SecurityUser;
import com.example.toeicwebsite.web.dto.exam.RegisterExamRequest;
import com.example.toeicwebsite.web.dto.exam.RegisterExamResponse;
import com.example.toeicwebsite.web.mapper.register_exam.ExamRegistrationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/exam-registration")
public class ExamRegistrationController {
    private final RegisterExam registerExam;
    private final GetPaymentInfo getPaymentInfo;
    private final ExamRegistrationMapper mapper;

    @PostMapping("/register")
    public ResponseEntity<RegisterExamResponse> registerExam(@AuthenticationPrincipal SecurityUser user,
                                                            @RequestBody RegisterExamRequest request) {
        RegisterExamCommand command = new RegisterExamCommand(
                user.getUser().getUserId(),
                new ExamScheduleId(request.scheduleId())
        );
        RegisterExamResult result = registerExam.handle(command);
        return ResponseEntity.ok(mapper.toResponse(result));
    }

    @GetMapping("/payment-info/{scheduleId}")
    public ResponseEntity<GetPaymentInfoResult> getPaymentInfo(
            @AuthenticationPrincipal SecurityUser user,
            @PathVariable UUID scheduleId) {
        GetPaymentInfoResult result = getPaymentInfo.getPaymentInfo(
                user.getUser().getUserId(),
                new ExamScheduleId(scheduleId)
        );
        return ResponseEntity.ok(result);
    }
}
