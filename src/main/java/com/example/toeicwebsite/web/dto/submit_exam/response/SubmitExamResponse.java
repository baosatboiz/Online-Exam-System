package com.example.toeicwebsite.web.dto.submit_exam.response;

import java.time.Instant;

public record SubmitExamResponse(
        Integer listeningCorrect,
        Integer readingCorrect,
        Integer totalCorrect,
        Integer listeningScore,
        Integer readingScore,
        Integer totalScore,
        Integer totalWrong,
        Integer totalUnanswered,
        Long totalTimeSeconds,
        Instant finishedAt
) {
}
