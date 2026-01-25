package com.example.toeicwebsite.infrastucture.persistence.repository_impl;

import com.example.toeicwebsite.domain.exam.model.Exam;
import com.example.toeicwebsite.domain.exam.repository.ExamRepository;
import com.example.toeicwebsite.infrastucture.persistence.jpa_repository.JpaExamRepository;
import com.example.toeicwebsite.domain.mapper.ExamMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ExamRepositoryImpl implements ExamRepository {
    private final JpaExamRepository jpaExamRepository;
    private final ExamMapper examMapper;

    @Override
    public Optional<Exam> findByBusinessId(UUID id) {
        return jpaExamRepository.findByBusinessId(id).map(examMapper::toDomain);
    }
}
