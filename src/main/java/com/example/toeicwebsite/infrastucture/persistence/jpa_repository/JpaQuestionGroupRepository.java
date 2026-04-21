package com.example.toeicwebsite.infrastucture.persistence.jpa_repository;

import com.example.toeicwebsite.infrastucture.persistence.entity.QuestionGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface JpaQuestionGroupRepository extends JpaRepository<QuestionGroupEntity, Long> {

    @Query("""
    SELECT qg
    FROM QuestionGroupEntity qg
    LEFT JOIN FETCH qg.questions q
    WHERE qg.exam.businessId = :examId AND qg.part = :part
    ORDER BY qg.id ASC
""")
    List<QuestionGroupEntity> findQuestionGroupsForPart(UUID examId, int part);
}
