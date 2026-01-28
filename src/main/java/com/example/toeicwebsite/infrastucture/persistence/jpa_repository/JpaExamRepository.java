package com.example.toeicwebsite.infrastucture.persistence.jpa_repository;

import com.example.toeicwebsite.infrastucture.persistence.entity.ExamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("select e from ExamEntity e join fetch e.questionGroups where e.businessId = :id")
    ExamEntity findFullExam(@Param("id") UUID businessId);
}
