package com.example.toeicwebsite.infrastucture.persistence.jpa_repository;

import com.example.toeicwebsite.infrastucture.persistence.entity.ExamAttemptEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
}
