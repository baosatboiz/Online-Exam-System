package com.example.toeicwebsite.infrastucture.persistence.repository_impl;

import com.example.toeicwebsite.domain.exam.model.Exam;
import com.example.toeicwebsite.domain.exam.model.ExamHeader;
import com.example.toeicwebsite.domain.exam.model.ExamId;
import com.example.toeicwebsite.domain.exam.repository.ExamRepository;
import com.example.toeicwebsite.infrastucture.persistence.entity.ExamEntity;
import com.example.toeicwebsite.infrastucture.persistence.jpa_repository.JpaExamRepository;
import com.example.toeicwebsite.infrastucture.persistence.mapper.ExamMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ExamRepositoryImpl implements ExamRepository {
    private final JpaExamRepository jpaExamRepository;
    private final ExamMapper examMapper;

    @Override
    public Exam save(Exam exam) {
        ExamEntity saved = jpaExamRepository.save(examMapper.toEntity(exam));
        return examMapper.toDomain(saved);
    }
    public Optional<Exam> findByBusinessId(UUID id) {
        return jpaExamRepository.findByBusinessId(id).map(examMapper::toDomain);
    }

    @Override
    public Integer findDurationByBusinessId(UUID id) {
        return jpaExamRepository.findDurationMinutesByBusinessId(id);
    }

    @Override
    public Exam findFullExam(UUID id) {
        return examMapper.toDomain(jpaExamRepository.findFullExam(id));
    }

    @Override
    public List<ExamHeader> findAll() {
        return jpaExamRepository.findAll().stream().map(examMapper::toHeader).toList();
    }

    @Override
    public Map<ExamId,Exam> findByBusinessIdIn(List<ExamId> ids) {
        List<UUID> examIds = ids.stream().map(ExamId::value).toList();
        return jpaExamRepository.findByBusinessIdIn(examIds)
                .stream()
                .collect(Collectors.toMap(e->new ExamId(e.getBusinessId()), examMapper::toDomain));
    }
}
