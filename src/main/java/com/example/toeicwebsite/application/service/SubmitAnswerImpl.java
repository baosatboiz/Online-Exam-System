package com.example.toeicwebsite.application.service;

import com.example.toeicwebsite.application.command.SubmitAnswerCommand;
import com.example.toeicwebsite.application.result.SubmitAnswerResult;
import com.example.toeicwebsite.application.usecase.SubmitAnswer;
import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttempt;
import com.example.toeicwebsite.domain.exam_attempt.repository.ExamAttemptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class SubmitAnswerImpl implements SubmitAnswer {

    private final ExamAttemptRepository examAttemptRepository;

    @Override
    public SubmitAnswerResult execute(SubmitAnswerCommand command) {
        Instant now = Instant.now();
        ExamAttempt attempt = examAttemptRepository.findByBusinessIdMinimal(command.examAttemptId().value())
                .orElseThrow(() -> new RuntimeException("Exam Attempt not found"));
        attempt.answer(command.questionId(), command.choiceKey(), now);
        examAttemptRepository.saveAnsweredQuestion(command.examAttemptId(), command.questionId(), command.choiceKey());
        return new SubmitAnswerResult(command.questionId(), command.choiceKey());
    }
}
