package com.example.toeicwebsite.application.service;

import com.example.toeicwebsite.application.mapper.GetAttemptQuestionsResultMapper;
import com.example.toeicwebsite.application.query.GetAttemptQuestionsQuery;
import com.example.toeicwebsite.application.result.GetAttemptQuestionsResult;
import com.example.toeicwebsite.application.usecase.GetAttemptQuestions;
import com.example.toeicwebsite.domain.exam.model.Exam;
import com.example.toeicwebsite.domain.exam.repository.ExamRepository;
import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttempt;
import com.example.toeicwebsite.domain.exam_attempt.repository.ExamAttemptRepository;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamSchedule;
import com.example.toeicwebsite.domain.exam_schedule.repository.ExamScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetAttemptQuestionsImpl implements GetAttemptQuestions {

    private final ExamAttemptRepository examAttemptRepository;
    private final ExamScheduleRepository examScheduleRepository;
    private final ExamRepository examRepository;
    @Qualifier("getAttemptQuestionsResultMapper")
    private final GetAttemptQuestionsResultMapper mapper;

    @Override
    public GetAttemptQuestionsResult handle(GetAttemptQuestionsQuery query) {
        ExamAttempt examAttempt = examAttemptRepository.findByBusinessId(query.examAttemptId().value())
                .orElseThrow(()-> new IllegalArgumentException("Exam attempt not found"));

        ExamSchedule examSchedule = examScheduleRepository.findByBusinessId(examAttempt.getExamScheduleId().value())
                .orElseThrow(()-> new IllegalArgumentException("Exam schedule not found"));

        UUID examId = examSchedule.getExamId().value();
        Exam exam = examRepository.findFullExam(examId);
        return mapper.toGetAttemptQuestionsResult(exam);
    }
}
