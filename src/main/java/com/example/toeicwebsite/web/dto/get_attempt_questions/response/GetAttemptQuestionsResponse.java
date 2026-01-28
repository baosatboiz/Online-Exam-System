package com.example.toeicwebsite.web.dto.get_attempt_questions.response;

import com.example.toeicwebsite.domain.exam.model.PartType;
import com.example.toeicwebsite.domain.question_bank.model.ChoiceKey;

import java.util.List;

public record GetAttemptQuestionsResponse(
        String title,
        Integer durationMinutes,
        List<PartResponse> parts
) {
    public record PartResponse(
            PartType partType,
            List<QuestionGroupResponse> questionGroups
    ) {}

    public record QuestionGroupResponse(
            String audioUrl,
            String passage,
            String imageUrl,
            List<QuestionResponse> questions
    ) {}

    public record QuestionResponse(
            String content,
            List<ChoiceResponse> choices
    ) {}

    public record ChoiceResponse(
            ChoiceKey key,
            String content
    ) {}
}
