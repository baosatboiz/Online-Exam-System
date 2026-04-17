package com.example.toeicwebsite.infrastucture.persistence.jpa_repository;

import com.example.toeicwebsite.domain.exam_schedule.model.ExamMode;
import com.example.toeicwebsite.infrastucture.persistence.entity.ExamScheduleEntity;
import com.example.toeicwebsite.infrastucture.persistence.projection.ExamScheduleProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaExamScheduleRepository extends JpaRepository<ExamScheduleEntity, Long> {
    Optional<ExamScheduleEntity> findByBusinessId(UUID businessId);
    @Query("select " +
            "e.businessId as businessId, " +
            "e.mode as mode, " +
            "e.openAt as openAt, " +
            "e.endAt as endAt, " +
            "e.partNumber as partNumber, " +
            "ex.businessId as examBusinessId " +
            "from ExamScheduleEntity e join e.exam ex " +
            "where(:mode is NULL or e.mode=:mode) " +
            "and (:partNumber IS NULL OR e.partNumber = :partNumber) " +
            "order by e.openAt desc, e.id desc")
    List<ExamScheduleProjection> findByMode(ExamMode mode, Integer partNumber, Pageable pageable);

    void deleteByBusinessId(UUID businessId);
}
