package com.example.toeicwebsite.infrastructure.persistence.repository;

import com.example.toeicwebsite.infrastructure.persistence.entity.ExamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamRepository extends JpaRepository<ExamEntity, Long> {

}
