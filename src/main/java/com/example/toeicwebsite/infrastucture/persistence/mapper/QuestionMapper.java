package com.example.toeicwebsite.infrastucture.persistence.mapper;

import com.example.toeicwebsite.domain.question_bank.model.Question;
import com.example.toeicwebsite.infrastucture.persistence.entity.ChoiceEntity;
import com.example.toeicwebsite.infrastucture.persistence.entity.QuestionEntity;
import com.example.toeicwebsite.infrastucture.persistence.entity.QuestionGroupEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = ChoiceMapper.class)
public interface QuestionMapper {
    @Mapping(target = "questionId", source = "id")
    @Mapping(target = "choices", source = "choices")
    Question toDomain(QuestionEntity entity);

    QuestionEntity toEntity(Question question);

    @AfterMapping
    default void afterToEntity(@MappingTarget QuestionEntity entity, Question domain) {
        for(ChoiceEntity choice: entity.getChoices()){
            choice.setQuestion(entity);
        }
    }
}
