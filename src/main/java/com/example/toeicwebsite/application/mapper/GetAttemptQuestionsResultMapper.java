package com.example.toeicwebsite.application.mapper;

import com.example.toeicwebsite.application.result.GetAttemptQuestionsResult;
import com.example.toeicwebsite.domain.exam.model.Exam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = PartResultMapper.class)
public interface GetAttemptQuestionsResultMapper {

    @Mapping(source = "part", target = "parts")
    GetAttemptQuestionsResult toGetAttemptQuestionsResult(Exam exam);
}
