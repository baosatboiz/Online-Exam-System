package com.example.toeicwebsite.infrastucture.persistence.repository_impl;

import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttempt;
import com.example.toeicwebsite.domain.exam_attempt.repository.ExamAttemptRepository;
import com.example.toeicwebsite.domain.exception.DomainException;
import com.example.toeicwebsite.domain.question_bank.model.ChoiceKey;
import com.example.toeicwebsite.domain.question_bank.model.Question;
import com.example.toeicwebsite.infrastucture.persistence.entity.ExamAttemptAnswerEntity;
import com.example.toeicwebsite.infrastucture.persistence.entity.ExamAttemptEntity;
import com.example.toeicwebsite.infrastucture.persistence.entity.ExamScheduleEntity;
import com.example.toeicwebsite.infrastucture.persistence.entity.QuestionEntity;
import com.example.toeicwebsite.infrastucture.persistence.jpa_repository.JpaExamAttemptAnswerRepository;
import com.example.toeicwebsite.infrastucture.persistence.jpa_repository.JpaExamAttemptRepository;
import com.example.toeicwebsite.infrastucture.persistence.jpa_repository.JpaExamScheduleRepository;
import com.example.toeicwebsite.infrastucture.persistence.jpa_repository.JpaQuestionRepository;
import com.example.toeicwebsite.infrastucture.persistence.mapper.ExamAttemptEntityMapper;
import com.example.toeicwebsite.infrastucture.persistence.mapper.ExamAttemptMapper;
import com.example.toeicwebsite.infrastucture.persistence.mapper.QuestionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ExamAttemptRepositoryImpl implements ExamAttemptRepository {

    private final JpaExamAttemptRepository jpaExamAttemptRepository;
    private final JpaExamScheduleRepository jpaExamScheduleRepository;
    private final JpaExamAttemptAnswerRepository jpaExamAttemptAnswerRepository;
    private final ExamAttemptEntityMapper examAttemptEntityMapper;
    private final ExamAttemptMapper examAttemptMapper;
    private final QuestionMapper questionMapper;
    private final JpaQuestionRepository jpaQuestionRepository;

    @Override
    public ExamAttempt save(ExamAttempt examAttempt, String userId) {
        ExamScheduleEntity examScheduleEntity = jpaExamScheduleRepository.findByBusinessId(examAttempt.getExamScheduleId().value())
                .orElseThrow(() -> new DomainException("Exam Schedule not found"));
        ExamAttemptEntity examAttemptEntity = examAttemptEntityMapper.toEntity(examAttempt, examScheduleEntity, userId);
        ExamAttemptEntity saved = jpaExamAttemptRepository.save(examAttemptEntity);
        return examAttemptMapper.toDomain(saved);
    }

    @Override
    public Optional<ExamAttempt> findByBusinessId(UUID businessId) {
        return jpaExamAttemptRepository.findByBusinessId(businessId).map(examAttemptMapper::toDomain);
    }

    @Override
    public void saveAnsweredQuestion(ExamAttempt examAttempt, Question question, ChoiceKey choiceKey) {
        ExamAttemptEntity examAttemptEntity = jpaExamAttemptRepository.findByBusinessId(examAttempt.getId().value())
                .orElseThrow(() -> new DomainException("Exam Attempt not found"));
        QuestionEntity questionEntity = jpaQuestionRepository.findById(question.getQuestionId())
                .orElseThrow(() -> new DomainException("Question not found"));
        ExamAttemptAnswerEntity answer = jpaExamAttemptAnswerRepository.findByQuestionIdAndExamAttemptId(question.getQuestionId(), examAttemptEntity.getId())
                .orElse(null);
        if (answer == null) {
            answer = new ExamAttemptAnswerEntity();
            answer.setExamAttempt(examAttemptEntity);
            answer.setQuestion(questionEntity);
        }
        answer.setChoiceKey(choiceKey);
        jpaExamAttemptAnswerRepository.save(answer);
    }
}
