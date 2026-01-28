package com.example.toeicwebsite.infrastucture.persistence.mapper;

import com.example.toeicwebsite.domain.question_bank.model.Choice;
import com.example.toeicwebsite.infrastucture.persistence.entity.ChoiceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChoiceMapper {
    @Mapping(source = "isCorrect", target = "correct")
    @Mapping(source = "label", target = "key")
    Choice toDomain(ChoiceEntity entity);

    @Mapping(source ="correct",target = "isCorrect")
    @Mapping(source = "key",target ="label")
    ChoiceEntity toEntity(Choice domain);
}
