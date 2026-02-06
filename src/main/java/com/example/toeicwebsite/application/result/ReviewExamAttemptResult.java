package com.example.toeicwebsite.application.result;

import com.example.toeicwebsite.domain.exam.model.PartType;
import com.example.toeicwebsite.domain.question_bank.model.ChoiceKey;

import java.util.List;

public record ReviewExamAttemptResult(
        String title,
        int duration,
        List<PartReviewResult> parts
) {
    public record PartReviewResult(
            PartType partType,
            List<QuestionGroupReviewResult> questionGroups
    ) {}

    public record QuestionGroupReviewResult(
            String audioUrl,
            String passage,
            String imageUrl,
            List<QuestionReviewResult> questions
    ) {}

    public record QuestionReviewResult(
            Long questionId,
            String content,
            List<ChoiceReviewResult> choices,
            ChoiceKey correctChoice,
            ChoiceKey userChoice,
            Boolean isCorrect,
            String explanation
    ) {}

    public record ChoiceReviewResult(
            ChoiceKey key,
            String content
    ) {}
}
