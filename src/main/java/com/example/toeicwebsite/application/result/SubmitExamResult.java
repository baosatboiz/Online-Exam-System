package com.example.toeicwebsite.application.result;

import java.time.Instant;

public record SubmitExamResult(
        Integer listeningScore,
        Integer readingScore,
        Integer totalScore,
        Instant startedAt,
        Instant finishedAt
) {
}
