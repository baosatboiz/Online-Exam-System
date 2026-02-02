package com.example.toeicwebsite.domain.exam_attempt.model;
import com.example.toeicwebsite.domain.exam.model.*;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamSchedule;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamScheduleId;
import com.example.toeicwebsite.domain.exception.DomainException;
import com.example.toeicwebsite.domain.question_bank.model.ChoiceKey;
import com.example.toeicwebsite.domain.question_bank.model.Question;
import com.example.toeicwebsite.domain.question_bank.model.QuestionGroup;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Getter @Setter
public class ExamAttempt {
    private ExamAttemptId id;
    private ExamScheduleId examScheduleId;
    private Instant startedAt;
    private Instant mustFinishedAt;
    private Instant finishedAt;
    private ExamStatus status;
    private Map<Long, ChoiceKey> answers = new HashMap<>();
    private Score score;

    public ExamAttempt() {
    }

    public ExamAttempt(ExamAttemptId examAttemptId, ExamSchedule examSchedule, Integer duration, Instant startedAt){
        if(!examSchedule.canStart(startedAt)) throw new DomainException("Exam is not open");
        this.examScheduleId = examSchedule.id();
        this.id = examAttemptId;
        this.startedAt = startedAt;
        mustFinishedAt = examSchedule.calculateMustFinishedAt(startedAt,duration);
        this.status = ExamStatus.IN_PROGRESS;
    }
    public static ExamAttempt start(ExamAttemptId examAttemptId,ExamSchedule examSchedule,Integer duration,Instant now){
       return new ExamAttempt(examAttemptId,examSchedule,duration,now);
    }
    public void answer(Long questionId,ChoiceKey choiceKey,Instant now){
        isInProgress(now);
        answers.put(questionId,choiceKey);
    }
    public void finish(Instant now){
        isInProgress(now);
        finishedAt = now;
        status = ExamStatus.SUBMITTED;
    }
    public void calculateScore(Exam exam){
        int listening =0;
        int reading =0;
        if(status==ExamStatus.IN_PROGRESS) throw new DomainException("Exam attempt is still in progress");
        for(Part part : exam.getPart()){
            for(QuestionGroup questionGroup : part.getQuestionGroups()){
                for(Question question : questionGroup.getQuestions())
                    if(answers.get(question.id())!=null&&answers.get(question.id()).equals(question.getCorrectChoice())){
                        if(part.type().isListening()) listening++;
                        else reading++;
                    };

            }
        }
//        return new Score(listening,reading);
        this.score = new Score(listening, reading);
    }
    public void restoreAnswer(Long questionId, ChoiceKey choiceKey) {
        this.answers.put(questionId, choiceKey);
    }
    public void expire(Instant now){
        if(status == ExamStatus.IN_PROGRESS){
            status=ExamStatus.EXPIRED;
            finishedAt = now;
        }
    }
    public void isInProgress(Instant now){
        if(status!=ExamStatus.IN_PROGRESS) throw new DomainException("Attempt already submitted");
        if(now.isAfter(mustFinishedAt)){
            expire(now);
            throw new DomainException("Attempt has expired");
        }
    }
}
