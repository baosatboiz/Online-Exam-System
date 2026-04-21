package com.example.toeicwebsite.application.result;

import com.example.toeicwebsite.domain.question_bank.model.ChoiceKey;

public record SubmitAnswerResult(
        Long questionId,
        ChoiceKey choiceKey
) {
}
