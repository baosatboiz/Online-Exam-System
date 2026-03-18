package com.example.toeicwebsite.application.result;

import com.example.toeicwebsite.domain.exam.model.ExamId;

public record GetExamResult(
        ExamId businessId,
        String title
) {
}
