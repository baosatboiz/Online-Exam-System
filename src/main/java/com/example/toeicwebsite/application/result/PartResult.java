package com.example.toeicwebsite.application.result;

import com.example.toeicwebsite.domain.exam.model.PartType;

import java.util.List;

public record PartResult(
        PartType partType,
        List<QuestionGroupResult> questionGroups
) {
}
