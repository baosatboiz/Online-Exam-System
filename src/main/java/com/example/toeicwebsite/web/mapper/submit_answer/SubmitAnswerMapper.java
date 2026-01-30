package com.example.toeicwebsite.web.mapper.submit_answer;

import com.example.toeicwebsite.application.command.SubmitAnswerCommand;
import com.example.toeicwebsite.application.result.SubmitAnswerResult;
import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttemptId;
import com.example.toeicwebsite.web.dto.submit_answer.request.SubmitAnswerRequest;
import com.example.toeicwebsite.web.dto.submit_answer.response.SubmitAnswerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface SubmitAnswerMapper {

    @Mapping(source = "attemptId", target = "examAttemptId")
    SubmitAnswerCommand toCommand(UUID attemptId, SubmitAnswerRequest request);

    default ExamAttemptId map(UUID attemptId) {
        return new ExamAttemptId(attemptId);
    }

    SubmitAnswerResponse toResponse(SubmitAnswerResult result);
}
