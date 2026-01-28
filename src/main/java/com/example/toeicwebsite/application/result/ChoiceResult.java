package com.example.toeicwebsite.application.result;

import com.example.toeicwebsite.domain.question_bank.model.ChoiceKey;

public record ChoiceResult(
        ChoiceKey key,
        String content
) {
}
