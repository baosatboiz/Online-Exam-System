package com.example.toeicwebsite.infrastucture.persistence.jpa_repository;

import com.example.toeicwebsite.domain.exam_schedule.model.ExamScheduleId;
import com.example.toeicwebsite.infrastucture.persistence.entity.ExamAttemptEntity;
import com.example.toeicwebsite.infrastucture.persistence.projection.TotalAttemtpProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaExamAttemptRepository extends JpaRepository<ExamAttemptEntity, Long> {
    Optional<ExamAttemptEntity> findByBusinessId(UUID businessId);

    @Query("""
        select distinct ea
        from ExamAttemptEntity ea
        left join fetch ea.answers
        where ea.businessId = :businessId
    """)
    Optional<ExamAttemptEntity> findFullByBusinessId(UUID businessId);
    @Query("""
    select count(ea.id) as total, ea.examSchedule.businessId as examScheduleId
    from ExamAttemptEntity ea
        where ea.examSchedule.businessId in :scheduleIds
    group by ea.examSchedule.businessId
    """)
    List<TotalAttemtpProjection> countTotalAttemptsIn(List<UUID> scheduleIds);
    @Query("""
    select ea
    from ExamAttemptEntity ea
    join fetch ea.examSchedule
    where ea.userId = :userId
    and ea.examSchedule.businessId in :ids
""")
    List<ExamAttemptEntity> findByUserIdAndExamScheduleIdsIn(String userId, List<UUID> ids);
}
