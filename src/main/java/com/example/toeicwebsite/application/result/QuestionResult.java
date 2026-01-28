package com.example.toeicwebsite.application.result;

import java.util.List;

public record QuestionResult(
        String content,
        List<ChoiceResult> choices
) {
}
