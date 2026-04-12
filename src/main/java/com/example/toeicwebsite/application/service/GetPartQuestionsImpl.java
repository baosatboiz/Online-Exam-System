package com.example.toeicwebsite.application.service;

import com.example.toeicwebsite.application.mapper.GetAttemptQuestionsResultMapper;
import com.example.toeicwebsite.application.query.GetPartQuestionQuery;
import com.example.toeicwebsite.application.result.GetAttemptQuestionsResult;
import com.example.toeicwebsite.application.usecase.GetPartQuestions;
import com.example.toeicwebsite.domain.exam.model.Exam;
import com.example.toeicwebsite.domain.exam.model.Part;
import com.example.toeicwebsite.domain.exam.model.PartType;
import com.example.toeicwebsite.domain.exam.repository.ExamRepository;
import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttempt;
import com.example.toeicwebsite.domain.exam_attempt.repository.ExamAttemptRepository;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamSchedule;
import com.example.toeicwebsite.domain.exam_schedule.repository.ExamScheduleRepository;
import com.example.toeicwebsite.domain.question_bank.model.QuestionGroup;
import com.example.toeicwebsite.domain.question_bank.repository.QuestionGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetPartQuestionsImpl implements GetPartQuestions {

    private final QuestionGroupRepository questionGroupRepository;
    private final ExamAttemptRepository examAttemptRepository;
    private final ExamScheduleRepository examScheduleRepository;
    private final ExamRepository examRepository;
    @Qualifier("getAttemptQuestionsResultMapper")
    private final GetAttemptQuestionsResultMapper mapper;

    @Override
    public GetAttemptQuestionsResult handle(GetPartQuestionQuery query) {
        ExamAttempt examAttempt = examAttemptRepository.findByBusinessId(query.examAttemptId().value())
                .orElseThrow(()-> new IllegalArgumentException("Exam attempt not found"));
        examAttempt.isInProgress(Instant.now());

        ExamSchedule examSchedule = examScheduleRepository.findByBusinessId(examAttempt.getExamScheduleId().value())
                .orElseThrow(()-> new IllegalArgumentException("Exam schedule not found"));

        UUID examId = examSchedule.getExamId().value();
        Exam exam = examRepository.findByBusinessId(examId).orElseThrow(()-> new IllegalArgumentException("Exam not found"));
        List<QuestionGroup> questionGroups = questionGroupRepository.findQuestionGroupsForPart(examId, query.partNumber());
        Part part = new Part(PartType.fromCode(query.partNumber()), questionGroups);

        GetAttemptQuestionsResult.PartResult partResult = mapper.toPartResult(part);
        return new GetAttemptQuestionsResult(
            exam.getTitle(),
            exam.getDuration() * 60,
            Collections.singletonList(partResult)
        );
    }
}
