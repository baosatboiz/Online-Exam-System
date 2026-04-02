package com.example.toeicwebsite.application.mapper;

import com.example.toeicwebsite.application.port.StoragePort;
import com.example.toeicwebsite.application.result.GetAttemptQuestionsResult;
import com.example.toeicwebsite.domain.exam.model.Exam;
import com.example.toeicwebsite.domain.exam.model.Part;
import com.example.toeicwebsite.domain.question_bank.model.Choice;
import com.example.toeicwebsite.domain.question_bank.model.Question;
import com.example.toeicwebsite.domain.question_bank.model.QuestionGroup;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class GetAttemptQuestionsResultMapper {
    @Autowired
    private StoragePort storagePort;

    @Mapping(source = "part", target = "parts")
    public abstract GetAttemptQuestionsResult toGetAttemptQuestionsResult(Exam exam);

    public abstract GetAttemptQuestionsResult.PartResult toPartResult(Part part);

    @Mapping(target = "audioUrl",ignore = true)
    @Mapping(target="imageUrl",ignore = true)
    public abstract GetAttemptQuestionsResult.QuestionGroupResult toQuestionGroupResult(QuestionGroup questionGroup);

    public abstract GetAttemptQuestionsResult.QuestionResult toQuestionResult(Question question);

    public abstract GetAttemptQuestionsResult.ChoiceResult toChoiceResult(Choice choice);
    @AfterMapping
    public void mapUrl(@MappingTarget GetAttemptQuestionsResult.QuestionGroupResult result,QuestionGroup questionGroup){
        result.setAudioUrl(storagePort.getFileUrl(questionGroup.getAudioUrl()));
        result.setImageUrl(storagePort.getFileUrl(questionGroup.getImageUrl()));
    }
}
