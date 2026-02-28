package com.example.toeicwebsite.application.service;

import com.example.toeicwebsite.application.command.SubmitAnswerCommand;
import com.example.toeicwebsite.application.result.SubmitAnswerResult;
import com.example.toeicwebsite.application.usecase.SubmitAnswer;
import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttempt;
import com.example.toeicwebsite.domain.exam_attempt.repository.ExamAttemptRepository;
import com.example.toeicwebsite.domain.question_bank.model.Question;
import com.example.toeicwebsite.domain.question_bank.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class SubmitAnswerImpl implements SubmitAnswer {

    private final ExamAttemptRepository examAttemptRepository;
    private final QuestionRepository questionRepository;

    @Override
    public SubmitAnswerResult execute(SubmitAnswerCommand command) {
        Instant now = Instant.now();
        ExamAttempt attempt = examAttemptRepository.findByBusinessId(command.examAttemptId().value())
                .orElseThrow(() -> new RuntimeException("Exam Attempt not found"));
        Question question = questionRepository.findById(command.questionId());
        attempt.answer(command.questionId(), command.choiceKey(), now);
        examAttemptRepository.saveAnsweredQuestion(attempt, question, command.choiceKey());
        return new SubmitAnswerResult(question.getContent(), command.choiceKey());
    }
}
