package com.example.toeicwebsite.application.result;

import java.time.Instant;

public record SubmitExamResult(
        Integer listeningCorrect,
        Integer readingCorrect,
        Integer totalCorrect,
        Integer totalWrong,
        Integer totalUnanswered,
        Instant startedAt,
        Instant finishedAt
) {
}
