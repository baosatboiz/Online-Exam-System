package com.example.toeicwebsite.repository;

import com.example.toeicwebsite.entity.ExamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamRepository extends JpaRepository<ExamEntity, Long> {

}
