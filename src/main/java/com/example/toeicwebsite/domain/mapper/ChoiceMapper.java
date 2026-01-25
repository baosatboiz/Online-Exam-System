package com.example.toeicwebsite.domain.mapper;

import com.example.toeicwebsite.domain.question_bank.model.Choice;
import com.example.toeicwebsite.infrastucture.persistence.entity.ChoiceEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChoiceMapper {
    static Choice toDomain(ChoiceEntity entity) {
        return new Choice(
                entity.getLabel(),
                entity.getContent(),
                entity.getIsCorrect()
        );
    }
}
