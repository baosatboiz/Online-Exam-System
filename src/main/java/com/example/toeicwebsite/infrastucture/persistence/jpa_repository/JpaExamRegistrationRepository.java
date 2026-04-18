package com.example.toeicwebsite.infrastucture.persistence.jpa_repository;

import com.example.toeicwebsite.infrastucture.persistence.entity.ExamRegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface JpaExamRegistrationRepository extends JpaRepository<ExamRegistrationEntity, UUID> {
    Long countByExamScheduleId(UUID examScheduleId);
    boolean existsByUserIdAndExamScheduleId(UUID userId, UUID examScheduleId);
    java.util.Optional<ExamRegistrationEntity> findByUserIdAndExamScheduleId(UUID userId, UUID examScheduleId);

    @Query("SELECT CASE WHEN COUNT(er) > 0 THEN true ELSE false END FROM ExamRegistrationEntity er, ExamScheduleEntity es " +
           "WHERE er.examScheduleId = es.businessId " +
           "AND er.userId = :userId " +
           "AND er.registrationStatus IN ('PENDING', 'CONFIRMED') " +
           "AND (es.openAt < :endAt AND es.endAt > :openAt)")
    boolean existsOverlappingSchedule(
            @Param("userId") UUID userId,
            @Param("openAt") LocalDateTime openAt,
            @Param("endAt") LocalDateTime endAt);
}
