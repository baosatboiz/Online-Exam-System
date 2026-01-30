package com.example.toeicwebsite.application.command;

import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttemptId;
import com.example.toeicwebsite.domain.question_bank.model.ChoiceKey;

public record SubmitAnswerCommand(
        ExamAttemptId examAttemptId,
        Long questionId,
        ChoiceKey choiceKey
) {}