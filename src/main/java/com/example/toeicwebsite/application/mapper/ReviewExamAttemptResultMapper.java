package com.example.toeicwebsite.application.mapper;

import com.example.toeicwebsite.application.result.ReviewExamAttemptResult;
import com.example.toeicwebsite.domain.exam.model.Exam;
import com.example.toeicwebsite.domain.exam.model.Part;
import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttempt;
import com.example.toeicwebsite.domain.question_bank.model.Choice;
import com.example.toeicwebsite.domain.question_bank.model.ChoiceKey;
import com.example.toeicwebsite.domain.question_bank.model.Question;
import com.example.toeicwebsite.domain.question_bank.model.QuestionGroup;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {
                ReviewExamAttemptResultMapper.PartReviewResultMapper.class,
                ReviewExamAttemptResultMapper.QuestionGroupReviewResultMapper.class,
                ReviewExamAttemptResultMapper.QuestionReviewResultMapper.class
        })
public interface ReviewExamAttemptResultMapper {
    @Mapping(source = "part", target = "parts")
    ReviewExamAttemptResult toReviewExamAttemptResult(Exam exam,
                                                      @Context ExamAttempt attempt);

    @Mapper(componentModel = "spring", uses = QuestionGroupReviewResultMapper.class)
    interface PartReviewResultMapper {
        ReviewExamAttemptResult.PartReviewResult toPartReviewResult(Part part,
                                                                    @Context ExamAttempt attempt);
    }

    @Mapper(componentModel = "spring", uses = QuestionReviewResultMapper.class)
    interface QuestionGroupReviewResultMapper {
        ReviewExamAttemptResult.QuestionGroupReviewResult toQuestionGroupReviewResult(QuestionGroup questionGroup,
                                                                                      @Context ExamAttempt attempt);
    }

    @Mapper(componentModel = "spring", uses = ChoiceReviewResultMapper.class)
    interface QuestionReviewResultMapper {
        List<ReviewExamAttemptResult.ChoiceReviewResult> mapChoices(List<Choice> choices);

        default ReviewExamAttemptResult.QuestionReviewResult toQuestionReviewResult(Question question,
                                                                            @Context ExamAttempt attempt) {

            ChoiceKey userChoice = attempt.getAnswers().get(question.id());
            boolean isCorrect = userChoice != null && userChoice.equals(question.getCorrectChoice());

            return new ReviewExamAttemptResult.QuestionReviewResult(
                    question.id(),
                    question.content(),
                    mapChoices(question.getChoices()),
                    question.getCorrectChoice(),
                    userChoice,
                    isCorrect,
                    question.getExplanation()
            );
        }
    }

    @Mapper(componentModel = "spring")
    interface ChoiceReviewResultMapper {
        ReviewExamAttemptResult.ChoiceReviewResult toChoiceReviewResult(Choice choice);
    }

}
