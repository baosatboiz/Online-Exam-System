package com.example.toeicwebsite.infrastucture.persistence.jpa_repository;

import com.example.toeicwebsite.infrastucture.persistence.entity.ExamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface JpaExamRepository extends JpaRepository<ExamEntity, Long> {
    Optional<ExamEntity> findByBusinessId(UUID businessId);
    @Query("""
        select e.durationMinutes
        from ExamEntity e
        where e.businessId = :businessId
    """)
    Integer findDurationMinutesByBusinessId(UUID businessId);
}
