package com.example.toeicwebsite.application.mapper;

import com.example.toeicwebsite.application.result.QuestionResult;
import com.example.toeicwebsite.domain.question_bank.model.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ChoiceResultMapper.class)
public interface QuestionResultMapper {

    @Mapping(source = "choices", target = "choices")
    QuestionResult toQuestionResult(Question question);
}
