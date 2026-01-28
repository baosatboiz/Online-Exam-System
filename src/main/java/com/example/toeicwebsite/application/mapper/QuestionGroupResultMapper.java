package com.example.toeicwebsite.application.mapper;

import com.example.toeicwebsite.application.result.QuestionGroupResult;
import com.example.toeicwebsite.domain.question_bank.model.QuestionGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = QuestionResultMapper.class)
public interface QuestionGroupResultMapper {

    @Mapping(source = "questions", target = "questions")
    QuestionGroupResult toQuestionGroupResult(QuestionGroup questionGroup);
}
