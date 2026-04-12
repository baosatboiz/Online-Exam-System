package com.example.toeicwebsite.infrastucture.persistence.repository_impl;

import com.example.toeicwebsite.domain.exam.model.PartType;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamMode;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamSchedule;
import com.example.toeicwebsite.domain.exam_schedule.repository.ExamScheduleRepository;
import com.example.toeicwebsite.infrastucture.persistence.entity.ExamEntity;
import com.example.toeicwebsite.infrastucture.persistence.jpa_repository.JpaExamRepository;
import com.example.toeicwebsite.infrastucture.persistence.jpa_repository.JpaExamScheduleRepository;
import com.example.toeicwebsite.infrastucture.persistence.mapper.ExamScheduleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ExamScheduleRepositoryImpl implements ExamScheduleRepository {
    private final JpaExamScheduleRepository jpaExamScheduleRepository;
    private final JpaExamRepository jpaExamRepository;
    private final ExamScheduleMapper examScheduleMapper;
    @Override
    public Optional<ExamSchedule> findByBusinessId(UUID id) {
        return jpaExamScheduleRepository.findByBusinessId(id).map(examScheduleMapper::toDomain);
    }

    @Override
    public List<ExamSchedule> findBySpecification(Integer page, ExamMode mode, PartType partType) {
        Integer partNumber = partType == null ? null : partType.getCode();
        Pageable pageable = page == null ? Pageable.unpaged() : PageRequest.of(page, 10);
        return jpaExamScheduleRepository.findByMode(mode, partNumber, pageable).stream().map(examScheduleMapper::toDomain).toList();
    }

    @Override
    public ExamSchedule save(ExamSchedule examSchedule) {
        ExamEntity exam = jpaExamRepository.findByBusinessId(examSchedule.getExamId().value()).orElse(null);
        return examScheduleMapper.toDomain(jpaExamScheduleRepository.save(examScheduleMapper.toEntity(examSchedule,exam)));
    }

    @Override
    public void delete(ExamSchedule examSchedule) {
        UUID id = examSchedule.getExamScheduleId().value();
        jpaExamScheduleRepository.deleteByBusinessId((id));
    }
}
