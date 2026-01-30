package com.example.toeicwebsite.web.dto.submit_answer.request;

import com.example.toeicwebsite.domain.question_bank.model.ChoiceKey;

import java.util.UUID;

public record SubmitAnswerRequest(
        Long questionId,
        ChoiceKey choiceKey
) {
}
