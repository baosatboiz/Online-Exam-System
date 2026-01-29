package com.example.toeicwebsite.application.mapper;

import com.example.toeicwebsite.application.result.GetAttemptQuestionsResult;
import com.example.toeicwebsite.domain.exam.model.Exam;
import com.example.toeicwebsite.domain.exam.model.Part;
import com.example.toeicwebsite.domain.question_bank.model.Choice;
import com.example.toeicwebsite.domain.question_bank.model.Question;
import com.example.toeicwebsite.domain.question_bank.model.QuestionGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GetAttemptQuestionsResultMapper {

    @Mapping(source = "part", target = "parts")
    GetAttemptQuestionsResult toGetAttemptQuestionsResult(Exam exam);

    @Mapper(componentModel = "spring")
    interface PartResultMapper {
        GetAttemptQuestionsResult.PartResult toPartResult(Part part);
    }

    @Mapper(componentModel = "spring")
    interface QuestionGroupResultMapper {
        GetAttemptQuestionsResult.QuestionGroupResult toQuestionGroupResult(QuestionGroup questionGroup);
    }

    @Mapper(componentModel = "spring")
    interface QuestionResultMapper {
        GetAttemptQuestionsResult.QuestionResult toQuestionResult(Question question);
    }

    @Mapper(componentModel = "spring")
    interface ChoiceResultMapper {
        GetAttemptQuestionsResult.ChoiceResult toChoiceResult(Choice choice);
    }
}
