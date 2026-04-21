package com.example.toeicwebsite.infrastucture.persistence.repository_impl;

import com.example.toeicwebsite.domain.question_bank.model.QuestionGroup;
import com.example.toeicwebsite.domain.question_bank.repository.QuestionGroupRepository;
import com.example.toeicwebsite.infrastucture.persistence.jpa_repository.JpaQuestionGroupRepository;
import com.example.toeicwebsite.infrastucture.persistence.mapper.QuestionGroupMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class QuestionGroupRepositoryImpl implements QuestionGroupRepository {

    private final JpaQuestionGroupRepository jpaQuestionGroupRepository;
    private final QuestionGroupMapper questionGroupMapper;

    @Override
    public List<QuestionGroup> findQuestionGroupsForPart(UUID examId, int part) {
        return jpaQuestionGroupRepository.findQuestionGroupsForPart(examId, part)
                .stream()
                .map(questionGroupMapper::toDomain)
                .toList();
    }
}
