package com.example.toeicwebsite.web.dto.create_exam.response;

public record CreateExamResponse(
        String id,
        String title,
        int totalQuestion
) {
}
