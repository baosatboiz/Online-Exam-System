package com.example.toeicwebsite.web.mapper;

import com.example.toeicwebsite.application.command.StartExamAttemptCommand;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamScheduleId;
import com.example.toeicwebsite.web.dto.request.StartExamAttemptRequest;
import org.springframework.stereotype.Component;

@Component
public class StartExamAttemptWebMapper {

    public static StartExamAttemptCommand toCommand(
            StartExamAttemptRequest request,
            String userId
    ) {
        return new StartExamAttemptCommand(
                new ExamScheduleId(request.examScheduleId()),
                userId
        );
    }
}
