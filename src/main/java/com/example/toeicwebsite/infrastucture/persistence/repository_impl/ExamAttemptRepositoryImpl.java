package com.example.toeicwebsite.infrastucture.persistence.repository_impl;

import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttempt;
import com.example.toeicwebsite.domain.exam_attempt.repository.ExamAttemptRepository;
import com.example.toeicwebsite.domain.exception.DomainException;
import com.example.toeicwebsite.infrastucture.persistence.entity.ExamAttemptEntity;
import com.example.toeicwebsite.infrastucture.persistence.entity.ExamScheduleEntity;
import com.example.toeicwebsite.infrastucture.persistence.jpa_repository.JpaExamAttemptRepository;
import com.example.toeicwebsite.infrastucture.persistence.jpa_repository.JpaExamScheduleRepository;
import com.example.toeicwebsite.infrastucture.persistence.mapper.ExamAttemptEntityMapper;
import com.example.toeicwebsite.infrastucture.persistence.mapper.ExamAttemptMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ExamAttemptRepositoryImpl implements ExamAttemptRepository {

    private final JpaExamAttemptRepository jpaExamAttemptRepository;
    private final JpaExamScheduleRepository jpaExamScheduleRepository;
    private final ExamAttemptEntityMapper examAttemptEntityMapper;
    private final ExamAttemptMapper examAttemptMapper;

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
}
