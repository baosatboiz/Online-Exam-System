package com.example.toeicwebsite.web.mapper.submit_exam;

import com.example.toeicwebsite.application.result.SubmitExamResult;
import com.example.toeicwebsite.web.dto.submit_exam.response.SubmitExamResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubmitExamMapper {

    @Mapping(
            target = "listeningScore",
            expression = "java(ToeicScoreConverter.listeningScore(result.listeningCorrect()))"
    )
    @Mapping(
            target = "readingScore",
            expression = "java(ToeicScoreConverter.readingScore(result.readingCorrect()))"
    )
    @Mapping(
            target = "totalScore",
            expression = "java("
                    + "ToeicScoreConverter.listeningScore(result.listeningCorrect())"
                    + " + "
                    + "ToeicScoreConverter.readingScore(result.readingCorrect())"
                    + ")"
    )
    @Mapping(
            target = "totalCorrect",
            expression = "java(result.listeningCorrect() + result.readingCorrect())"
    )
    @Mapping(target = "totalTimeSeconds", source = "totalTimeSeconds")
    @Mapping(target = "finishedAt", source = "result.finishedAt")
    SubmitExamResponse toResponse(
            SubmitExamResult result,
            Long totalTimeSeconds
    );
}