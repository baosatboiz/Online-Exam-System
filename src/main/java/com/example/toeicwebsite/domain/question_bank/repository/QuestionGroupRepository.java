package com.example.toeicwebsite.domain.question_bank.repository;

import com.example.toeicwebsite.domain.question_bank.model.QuestionGroup;

import java.util.List;
import java.util.UUID;

public interface QuestionGroupRepository {
    List<QuestionGroup> findQuestionGroupsForPart(UUID examId, int part);
}
