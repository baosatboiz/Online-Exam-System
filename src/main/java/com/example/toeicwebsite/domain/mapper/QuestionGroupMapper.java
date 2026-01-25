package com.example.toeicwebsite.domain.mapper;

import com.example.toeicwebsite.domain.exam.model.Part;
import com.example.toeicwebsite.domain.exam.model.PartType;
import com.example.toeicwebsite.domain.question_bank.model.QuestionGroup;
import com.example.toeicwebsite.infrastucture.persistence.entity.QuestionGroupEntity;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = QuestionMapper.class)
public interface QuestionGroupMapper {
    QuestionGroup toDomain(QuestionGroupEntity entity);

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
                .toList();
    }
}
