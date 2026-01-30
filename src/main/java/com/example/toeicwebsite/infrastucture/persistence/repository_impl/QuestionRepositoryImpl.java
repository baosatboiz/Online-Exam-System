package com.example.toeicwebsite.infrastucture.persistence.repository_impl;

import com.example.toeicwebsite.domain.question_bank.model.Question;
import com.example.toeicwebsite.domain.question_bank.repository.QuestionRepository;
import com.example.toeicwebsite.infrastucture.persistence.entity.QuestionEntity;
import com.example.toeicwebsite.infrastucture.persistence.jpa_repository.JpaQuestionRepository;
import com.example.toeicwebsite.infrastucture.persistence.mapper.QuestionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuestionRepositoryImpl implements QuestionRepository {

    private final JpaQuestionRepository jpaQuestionRepository;
    private final QuestionMapper questionMapper;

    @Override
    public Question findById(Long questionId) {
        QuestionEntity entity = jpaQuestionRepository.findById(questionId).orElse(null);
        return questionMapper.toDomain(entity);
    }
}
