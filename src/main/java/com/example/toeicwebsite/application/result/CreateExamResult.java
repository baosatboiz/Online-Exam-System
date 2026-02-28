package com.example.toeicwebsite.application.result;

import com.example.toeicwebsite.domain.exam.model.ExamId;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateExamResult(
        ExamId id,
        String title,
        int totalQuestions
) {}