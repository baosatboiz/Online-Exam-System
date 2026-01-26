package com.example.toeicwebsite.infrastucture.persistence.mapper;

import com.example.toeicwebsite.domain.question_bank.model.Question;
import com.example.toeicwebsite.infrastucture.persistence.entity.QuestionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ChoiceMapper.class)
public interface QuestionMapper {
    @Mapping(target = "questionId", source = "id")
    @Mapping(target = "choices", source = "choices")
    Question toDomain(QuestionEntity entity);
}
