package com.example.toeicwebsite.domain.question_bank.repository;

import com.example.toeicwebsite.domain.question_bank.model.Question;

public interface QuestionRepository {
    Question findById(Long questionId);
}
