package com.example.toeicwebsite.web.dto.review_exam_attempt.response;

import com.example.toeicwebsite.domain.exam.model.PartType;
import com.example.toeicwebsite.domain.question_bank.model.ChoiceKey;

import java.util.List;

public record ReviewExamAttemptResponse(
        String title,
        Integer durationMinutes,
        List<PartReviewResponse> parts
) {
    public record PartReviewResponse(
            PartType partType,
            List<QuestionGroupReviewResponse> questionGroups
    ) {}

    public record QuestionGroupReviewResponse(
            String audioUrl,
            String passage,
            String imageUrl,
            List<QuestionReviewResponse> questions
    ) {}

    public record QuestionReviewResponse(
            Long questionId,
            String content,
            List<ChoiceReviewResponse> choices,
            ChoiceKey correctChoice,
            ChoiceKey userChoice,
            Boolean isCorrect,
            String explanation
    ) {}

    public record ChoiceReviewResponse(
            ChoiceKey key,
            String content
    ) {}
}
