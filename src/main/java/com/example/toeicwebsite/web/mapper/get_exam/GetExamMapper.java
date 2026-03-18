package com.example.toeicwebsite.web.mapper.get_exam;

import com.example.toeicwebsite.application.result.GetExamResult;
import com.example.toeicwebsite.domain.exam.model.ExamId;
import com.example.toeicwebsite.web.dto.get_exam.GetExamReponse;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface GetExamMapper {
    GetExamReponse toResponse(GetExamResult result);
    default UUID map(ExamId examId){
        return examId.value();
    }
}
