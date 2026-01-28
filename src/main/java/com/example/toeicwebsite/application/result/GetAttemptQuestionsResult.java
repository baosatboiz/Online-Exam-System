package com.example.toeicwebsite.application.result;

import java.util.List;

public record GetAttemptQuestionsResult(
    String title,
    int duration,
    List<PartResult> parts
) {
}
