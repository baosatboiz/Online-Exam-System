package com.example.toeicwebsite.application.mapper;

import com.example.toeicwebsite.application.result.PartResult;
import com.example.toeicwebsite.domain.exam.model.Part;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = QuestionGroupResultMapper.class)
public interface PartResultMapper {

    @Mapping(source = "partType", target = "partType")
    @Mapping(source = "questionGroups", target = "questionGroups")
    PartResult toPartResult(Part part);
}
