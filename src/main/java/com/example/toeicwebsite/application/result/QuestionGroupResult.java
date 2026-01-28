package com.example.toeicwebsite.application.result;

import java.util.List;

public record QuestionGroupResult(
    String audioUrl,
    String passage,
    String imageUrl,
    List<QuestionResult> questions
) {
}
