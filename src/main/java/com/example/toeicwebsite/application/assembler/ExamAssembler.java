package com.example.toeicwebsite.application.assembler;

import com.example.toeicwebsite.application.command.CreateExamCommand;
import com.example.toeicwebsite.domain.exam.model.Exam;
import com.example.toeicwebsite.domain.exam.model.Part;
import com.example.toeicwebsite.domain.question_bank.model.Choice;
import com.example.toeicwebsite.domain.question_bank.model.ChoiceKey;
import com.example.toeicwebsite.domain.question_bank.model.Question;
import com.example.toeicwebsite.domain.question_bank.model.QuestionGroup;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ExamAssembler {
    public Exam from(CreateExamCommand command) {
        List<Part> parts = command.parts().stream().map(this::from).toList();
        return Exam.create(parts,command.title(),command.duration());
    }
    public Part from(CreateExamCommand.PartCommand command) {
        List<QuestionGroup> questionGroups = command.questionGroups().stream().map(this::from).toList();
        return Part.create(command.partType(),questionGroups);
    }
    public QuestionGroup from(CreateExamCommand.PartCommand.QuestionGroupCommand command){
        List<Question> questions = command.questions().stream().map(this::from).toList();
        return QuestionGroup.create(command.audioUrl(),command.passage(),command.imageUrl(),questions);
    }
    public Question from(CreateExamCommand.PartCommand.QuestionGroupCommand.QuestionCommand command){
        Choice correctChoice = command.choices()
                .stream().filter(choiceCommand -> choiceCommand.correct())
                .map(choiceCommand -> new Choice(choiceCommand.choiceKey(),choiceCommand.content(), true)).findFirst().orElseThrow();
        List<Choice> choices = command.choices().stream().map(choiceCommand -> new Choice(choiceCommand.choiceKey(),choiceCommand.content(),choiceCommand.correct())).toList();
        return Question.create(command.content(),choices,command.explanation());
    }

}
