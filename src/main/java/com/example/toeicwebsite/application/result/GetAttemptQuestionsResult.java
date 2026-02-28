package com.example.toeicwebsite.application.result;

import com.example.toeicwebsite.domain.exam.model.PartType;
import com.example.toeicwebsite.domain.question_bank.model.ChoiceKey;

import java.util.List;

public record GetAttemptQuestionsResult(
    String title,
    int duration,
    List<PartResult> parts
) {
    public record PartResult(
            PartType partType,
            List<QuestionGroupResult> questionGroups
    ) {}

    public record QuestionGroupResult(
            String audioUrl,
            String passage,
            String imageUrl,
            List<QuestionResult> questions
    ) {}

    public record QuestionResult(
            Long questionId,
            String content,
            List<ChoiceResult> choices
    ) {}

    public record ChoiceResult(
            ChoiceKey key,
            String content
    ) {}
}
