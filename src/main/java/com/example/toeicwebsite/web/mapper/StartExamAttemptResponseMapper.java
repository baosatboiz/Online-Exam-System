package com.example.toeicwebsite.web.mapper;

import com.example.toeicwebsite.application.result.StartExamAttemptResult;
import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttemptId;
import com.example.toeicwebsite.web.dto.response.StartExamAttemptResponse;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Duration;
import java.time.Instant;

@Mapper(componentModel = "spring")
public interface StartExamAttemptResponseMapper {

    @Mapping(target = "attemptId", source = "attemptId")
    @Mapping(target = "remainingSeconds", expression = "java(remainingSeconds(result, now))")
    @Mapping(target = "mustFinishedAt", source = "mustFinishedAt")
    StartExamAttemptResponse toResponse(
            StartExamAttemptResult result,
            @Context Instant now
    );

    default String map(ExamAttemptId id) {
        return id.value().toString();
    }

    default long remainingSeconds(StartExamAttemptResult result, Instant now) {
        return Duration.between(now, result.mustFinishedAt()).getSeconds();
    }
}


