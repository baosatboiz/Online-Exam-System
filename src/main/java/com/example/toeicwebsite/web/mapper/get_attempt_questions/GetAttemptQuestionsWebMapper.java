package com.example.toeicwebsite.web.mapper.get_attempt_questions;

import com.example.toeicwebsite.application.query.GetAttemptQuestionsQuery;
import com.example.toeicwebsite.application.result.GetAttemptQuestionsResult;
import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttemptId;
import com.example.toeicwebsite.web.dto.get_attempt_questions.request.GetAttemptQuestionsRequest;
import com.example.toeicwebsite.web.dto.get_attempt_questions.response.GetAttemptQuestionsResponse;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface GetAttemptQuestionsWebMapper {

//    @Mapping(source = "request.attemptId", target = "examAttemptId")
//    @Mapping(target = "userId", expression = "java(userId)")
//    GetAttemptQuestionsQuery toQuery(GetAttemptQuestionsRequest request, @Context String userId);
//
//    default ExamAttemptId map(UUID id) {
//        return new ExamAttemptId(id);
//    }

    @Mapping(source = "parts", target = "parts")
    @Mapping(source = "duration", target = "durationMinutes")
    GetAttemptQuestionsResponse toResponse(
            GetAttemptQuestionsResult result);
}
