package com.example.toeicwebsite.domain.exam_attempt.model;
import com.example.toeicwebsite.domain.exam.model.*;
import com.example.toeicwebsite.domain.exam_attempt.exception.DomainException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class ExamAttempt {
    private ExamAttemptId id;
    private ExamId examId;
    private Instant startedAt;
    private Instant mustFinishedAt;
    private Instant finishedAt;
    private ExamStatus status;
    private Map<Long, ChoiceKey> answers = new HashMap<>();
    private ExamAttempt(ExamAttemptId examAttemptId,Exam exam,Instant startedAt){
        if(!exam.canStart(startedAt)) throw new DomainException("Exam is not open");
        this.examId = exam.id();
        this.id = examAttemptId;
        this.startedAt = startedAt;
        mustFinishedAt = exam.calculateMustFinishedAt(startedAt);
        this.status = ExamStatus.IN_PROGRESS;
    }
    public static ExamAttempt start(ExamAttemptId examAttemptId,Exam exam,Instant now){
       return new ExamAttempt(examAttemptId,exam,now);
    }
    public void answer(Exam exam,Long questionId,ChoiceKey choiceKey,Instant now){
        isInProgress(now);
        answers.put(questionId,choiceKey);
    }
    public void finish(Instant now){
        isInProgress(now);
        finishedAt = now;
        status = ExamStatus.SUBMITTED;
    }
    public Score calculateScore(Exam exam){
        int listening =0;
        int reading =0;
        if(status==ExamStatus.IN_PROGRESS) throw new DomainException("Exam attempt is still in progress");
        for(Part part : exam.getPart()){
            for(QuestionGroup questionGroup : part.getQuestionGroups()){
                for(Question question : questionGroup.getQuestions())
                    if(answers.get(question.id())!=null&&answers.get(question.id()).equals(question.getCorrectChoice().getKey())){
                        if(part.type().isListening()) listening++;
                        else reading++;
                    };

            }
        }
        return new Score(listening,reading);
    }
    public void expire(Instant now){
        if(status == ExamStatus.IN_PROGRESS){
            status=ExamStatus.EXPIRED;
            finishedAt = now;
        }
    }
    void isInProgress(Instant now){
        if(status!=ExamStatus.IN_PROGRESS) throw new DomainException("Attempt already submitted");
        if(now.isAfter(mustFinishedAt)){
            expire(now);
            throw new DomainException("Attempt has expired");
        }
    }
}
