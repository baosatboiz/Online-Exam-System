package com.example.toeicwebsite.domain.mapper;

import com.example.toeicwebsite.domain.question_bank.model.Question;
import com.example.toeicwebsite.infrastucture.persistence.entity.QuestionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = ChoiceMapper.class)
public interface QuestionMapper {
     default Question toDomain(QuestionEntity entity) {
        return new Question(
                entity.getId(),
                entity.getQuestionNo(),
                entity.getContent(),
                entity.getChoices().stream()
                        .map(ChoiceMapper::toDomain)
                        .toList(),
                entity.getExplanation()
        );
    }
}
