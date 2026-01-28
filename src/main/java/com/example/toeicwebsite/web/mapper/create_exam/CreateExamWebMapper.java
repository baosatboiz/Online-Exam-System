package com.example.toeicwebsite.web.mapper.create_exam;

import com.example.toeicwebsite.application.command.CreateExamCommand;
import com.example.toeicwebsite.application.command.CreateExamCommand.*;
import com.example.toeicwebsite.application.command.CreateExamCommand.PartCommand.*;
import com.example.toeicwebsite.application.command.CreateExamCommand.PartCommand.QuestionGroupCommand;
import com.example.toeicwebsite.application.command.CreateExamCommand.PartCommand.QuestionGroupCommand.QuestionCommand;
import com.example.toeicwebsite.application.command.CreateExamCommand.PartCommand.QuestionGroupCommand.QuestionCommand.ChoiceCommand;


import com.example.toeicwebsite.application.result.CreateExamResult;
import com.example.toeicwebsite.domain.exam.model.ExamId;
import com.example.toeicwebsite.domain.exam.model.PartType;
import com.example.toeicwebsite.web.dto.create_exam.request.*;
import com.example.toeicwebsite.web.dto.create_exam.response.CreateExamResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CreateExamWebMapper {

    @Mapping(target = "duration", source = "durationMinutes")
    @Mapping(target = "parts", source = "questionGroups", qualifiedByName = "mapGroupsToParts")
    CreateExamCommand toCommand(ExamRequest request);

    CreateExamResponse toResponse(CreateExamResult result);
    default String map(ExamId id){ return id.value().toString();}

    @Named("mapGroupsToParts")
    default List<PartCommand> ToParts(List<QuestionGroupRequest> requests) {
        if (requests == null) return List.of();

        Map<Integer, List<QuestionGroupRequest>> groupedByPart = requests.stream()
                .collect(Collectors.groupingBy(QuestionGroupRequest::getPart));

        return groupedByPart.entrySet().stream()
                .map(entry -> {
                    PartType partType = PartType.fromCode(entry.getKey());
                    List<QuestionGroupCommand> groups = entry.getValue().stream()
                            .map(this::toQuestionGroupCommand)
                            .toList();
                    return new PartCommand(partType, groups);
                })
                .toList();
    }

    @Mapping(target = "passage", source = "passageText")
    @Mapping(target = "questions", source = "questions")
    QuestionGroupCommand toQuestionGroupCommand(QuestionGroupRequest request);

    @Mapping(target = "choices", source = "choices")
    QuestionCommand toQuestionCommand(QuestionRequest request);

    @Mapping(target = "choiceKey", source = "label")
    @Mapping(target = "correct", source = "isCorrect")
    ChoiceCommand toChoiceCommand(ChoiceRequest request);
}