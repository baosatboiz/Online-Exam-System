package com.example.toeicwebsite.web.dto.submit_answer.response;

import com.example.toeicwebsite.domain.question_bank.model.ChoiceKey;

public record SubmitAnswerResponse(
    String content,
    ChoiceKey choiceKey
) {
}
