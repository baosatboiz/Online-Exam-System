package com.example.toeicwebsite.application.command;

import com.example.toeicwebsite.domain.exam.model.PartType;
import com.example.toeicwebsite.domain.question_bank.model.ChoiceKey;

import java.util.List;

public record CreateExamCommand(
        String title,
        int duration,
        List<PartCommand> parts
) {
    public record PartCommand(
            PartType partType,
            List<QuestionGroupCommand> questionGroups
    ){
        public record QuestionGroupCommand(
            String audioUrl,
            String passage,
            String imageUrl,
            List<QuestionCommand> questions
        ){
            public record QuestionCommand(
                    String content,
                    String explanation,
                    List<ChoiceCommand> choices
            ){
                public record ChoiceCommand(
                        ChoiceKey choiceKey,
                        String content,
                        boolean correct
                ){}
            }
        }
    }
}
