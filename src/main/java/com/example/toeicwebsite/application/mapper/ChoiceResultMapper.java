package com.example.toeicwebsite.application.mapper;

import com.example.toeicwebsite.application.result.ChoiceResult;
import com.example.toeicwebsite.domain.question_bank.model.Choice;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChoiceResultMapper {

    ChoiceResult toChoiceResult(Choice choice);
}
