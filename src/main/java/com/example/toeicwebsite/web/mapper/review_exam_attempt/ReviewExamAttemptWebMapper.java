package com.example.toeicwebsite.web.mapper.review_exam_attempt;

import com.example.toeicwebsite.application.result.ReviewExamAttemptResult;
import com.example.toeicwebsite.web.dto.review_exam_attempt.response.ReviewExamAttemptResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewExamAttemptWebMapper {

    @Mapping(source = "duration", target = "durationMinutes")
    @Mapping(source = "parts", target = "parts")
    ReviewExamAttemptResponse toResponse(ReviewExamAttemptResult result);
}
