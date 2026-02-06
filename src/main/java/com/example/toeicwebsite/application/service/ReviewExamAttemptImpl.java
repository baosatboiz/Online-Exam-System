package com.example.toeicwebsite.application.service;

import com.example.toeicwebsite.application.mapper.ReviewExamAttemptResultMapper;
import com.example.toeicwebsite.application.query.ReviewExamAttemptQuery;
import com.example.toeicwebsite.application.result.ReviewExamAttemptResult;
import com.example.toeicwebsite.application.usecase.ReviewExamAttempt;
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
public class ReviewExamAttemptImpl implements ReviewExamAttempt {

    private final ExamAttemptRepository examAttemptRepository;
    private final ExamScheduleRepository examScheduleRepository;
    private final ExamRepository examRepository;
    @Qualifier("reviewExamAttemptResultMapper")
    private final ReviewExamAttemptResultMapper mapper;

    @Override
    public ReviewExamAttemptResult handle(ReviewExamAttemptQuery query) {
        ExamAttempt examAttempt = examAttemptRepository.findByBusinessId(query.examAttemptId().value())
                .orElseThrow(() -> new IllegalArgumentException("Exam attempt not found"));

        if (!examAttempt.getStatus().name().equals("SUBMITTED")) {
            throw new IllegalStateException("Cannot review an exam attempt that is not submitted");
        }

        ExamSchedule schedule = examScheduleRepository.findByBusinessId(examAttempt.getExamScheduleId().value())
                .orElseThrow(() -> new IllegalArgumentException("Exam schedule not found"));

        UUID examId = schedule.getExamId().value();
        Exam exam = examRepository.findFullExam(examId);
        ReviewExamAttemptResult result = mapper.toReviewExamAttemptResult(exam, examAttempt);
        return result;
    }
}
