package com.example.toeicwebsite.web.dto.submit_exam.response;

import java.time.Instant;

public record SubmitExamResponse(
        Integer listeningScore,
        Integer readingScore,
        Integer totalScore,
        Long totalTimeSeconds,
        Instant finishedAt
) {
}
