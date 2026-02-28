package com.example.toeicwebsite.infrastucture.persistence.repository_impl;

import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttempt;
import com.example.toeicwebsite.domain.exam_attempt.model.ExamStatus;
import com.example.toeicwebsite.domain.exam_attempt.repository.ExamAttemptRepository;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamScheduleId;
import com.example.toeicwebsite.domain.exception.DomainNotFoundException;
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
import com.example.toeicwebsite.infrastucture.persistence.mapper.ExamAttemptEntityUpdateMapper;
import com.example.toeicwebsite.infrastucture.persistence.mapper.ExamAttemptMapper;
import com.example.toeicwebsite.infrastucture.persistence.projection.TotalAttemtpProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ExamAttemptRepositoryImpl implements ExamAttemptRepository {

    private final JpaExamAttemptRepository jpaExamAttemptRepository;
    private final JpaExamScheduleRepository jpaExamScheduleRepository;
    private final JpaExamAttemptAnswerRepository jpaExamAttemptAnswerRepository;
    private final ExamAttemptEntityMapper examAttemptEntityMapper;
    private final ExamAttemptMapper examAttemptMapper;
    private final ExamAttemptEntityUpdateMapper examAttemptEntityUpdateMapper;
    private final JpaQuestionRepository jpaQuestionRepository;

    @Override
    public ExamAttempt save(ExamAttempt examAttempt, String userId) {
        ExamScheduleEntity examScheduleEntity = jpaExamScheduleRepository.findByBusinessId(examAttempt.getExamScheduleId().value())
                .orElseThrow(() -> new DomainNotFoundException("Exam Schedule not found"));
        ExamAttemptEntity examAttemptEntity = examAttemptEntityMapper.toEntity(examAttempt, examScheduleEntity, userId);
        ExamAttemptEntity saved = jpaExamAttemptRepository.save(examAttemptEntity);
        return examAttemptMapper.toDomain(saved);
    }

    @Override
    public ExamAttempt update(ExamAttempt examAttempt) {
        ExamAttemptEntity entity = jpaExamAttemptRepository.findFullByBusinessId(examAttempt.getId().value())
                .orElseThrow(() -> new DomainNotFoundException("Exam attempt not found"));

        examAttemptEntityUpdateMapper.updateEntity(examAttempt, entity);

        ExamAttemptEntity saved = jpaExamAttemptRepository.save(entity);
        return examAttemptMapper.toDomain(saved);
    }

    @Override
    public Optional<ExamAttempt> findByBusinessId(UUID businessId) {
        return jpaExamAttemptRepository.findByBusinessId(businessId).map(examAttemptMapper::toDomain);
    }

    @Override
    public void saveAnsweredQuestion(ExamAttempt examAttempt, Question question, ChoiceKey choiceKey) {
        ExamAttemptEntity examAttemptEntity = jpaExamAttemptRepository.findByBusinessId(examAttempt.getId().value())
                .orElseThrow(() -> new DomainNotFoundException("Exam Attempt not found"));
        QuestionEntity questionEntity = jpaQuestionRepository.findById(question.getQuestionId())
                .orElseThrow(() -> new DomainNotFoundException("Question not found"));
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

    @Override
    public Map<ExamScheduleId, Long> countTotalAttemptsIn(List<ExamScheduleId> ids) {
        List<UUID> scheduleIds = ids.stream().map(ExamScheduleId::value).toList();
        return jpaExamAttemptRepository.countTotalAttemptsIn(scheduleIds)
                .stream()
                .collect(Collectors.toMap(p->new ExamScheduleId(p.getExamScheduleId()), TotalAttemtpProjection::getTotal));
    }

    @Override
    public Map<ExamScheduleId, ExamStatus> findByUserIdAndScheduleIdsIn(String userId, List<ExamScheduleId> ids) {
        List<UUID> scheduleIds = ids.stream().map(ExamScheduleId::value).toList();
        return jpaExamAttemptRepository.findByUserIdAndExamScheduleIdsIn(userId,scheduleIds)
                .stream()
                .collect(Collectors.toMap(
                        e-> new ExamScheduleId(e.getExamSchedule().getBusinessId()),
                                ExamAttemptEntity::getStatus,
                        (existing,replacement)->replacement));
    }
}
