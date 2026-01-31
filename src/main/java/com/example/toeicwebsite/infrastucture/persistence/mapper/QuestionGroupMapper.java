package com.example.toeicwebsite.infrastucture.persistence.mapper;

import com.example.toeicwebsite.domain.exam.model.Part;
import com.example.toeicwebsite.domain.exam.model.PartType;
import com.example.toeicwebsite.domain.question_bank.model.QuestionGroup;
import com.example.toeicwebsite.infrastucture.persistence.entity.QuestionEntity;
import com.example.toeicwebsite.infrastucture.persistence.entity.QuestionGroupEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = QuestionMapper.class)
public interface QuestionGroupMapper {
    @Mapping(source = "passageText", target = "passage")
    QuestionGroup toDomain(QuestionGroupEntity entity);

    @Mapping(source = "passage",target="passageText")
    QuestionGroupEntity toEntity(QuestionGroup domain);
    default List<Part> toParts(List<QuestionGroupEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return List.of();
        }

        // 1️⃣ group theo PartType
        Map<PartType, List<QuestionGroupEntity>> grouped = entities.stream()
                .collect(Collectors.groupingBy(e -> PartType.fromCode(e.getPart())));

        // 2️⃣ map từng group -> Part
        return grouped.entrySet().stream()
                .map(entry -> {
                    PartType partType = entry.getKey();

                    List<QuestionGroup> questionGroups =
                            entry.getValue().stream()
                                    .map(this::toDomain)
                                    .toList();

                    return new Part(partType, questionGroups);
                })
                .sorted(Comparator.comparing(Part::type))
                .toList();
    }
    default List<QuestionGroupEntity> toQGE(List<Part> part){
        return part.stream()
                .flatMap(p->p.getQuestionGroups()
                        .stream()
                        .map(q->{
                            QuestionGroupEntity qGE = this.toEntity(q);
                            qGE.setPart(p.type().getCode());
                            return qGE;
                        }))
                .toList();
    }
    @AfterMapping
    default void afterToEntity(@MappingTarget QuestionGroupEntity questionGroupEntity,QuestionGroup domain) {
        for(QuestionEntity question : questionGroupEntity.getQuestions()){
            question.setGroup(questionGroupEntity);
        }
    }
}