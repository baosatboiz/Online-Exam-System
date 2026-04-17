package com.example.toeicwebsite.domain.exam_attempt.model;
import com.example.toeicwebsite.domain.exam.model.*;
import com.example.toeicwebsite.domain.exam_registration.model.ExamRegistration;
import com.example.toeicwebsite.domain.exam_registration.model.ExamRegistrationId;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamSchedule;
import com.example.toeicwebsite.domain.exam_schedule.model.ExamScheduleId;
import com.example.toeicwebsite.domain.exception.BusinessRuleException;
import com.example.toeicwebsite.domain.question_bank.model.ChoiceKey;
import com.example.toeicwebsite.domain.question_bank.model.Question;
import com.example.toeicwebsite.domain.question_bank.model.QuestionGroup;
import com.example.toeicwebsite.domain.user.model.User;
import com.example.toeicwebsite.domain.user.model.UserId;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter @Setter
public class ExamAttempt {
    private ExamAttemptId id;
    private ExamScheduleId examScheduleId;
    private UserId userId;
    private Instant startedAt;
    private Instant mustFinishedAt;
    private Instant finishedAt;
    private ExamStatus status;
    private Map<Long, ChoiceKey> answers = new HashMap<>();
    private Score score;
    private ExamRegistrationId registrationId;

    public ExamAttempt() {
    }

    public ExamAttempt(ExamAttemptId examAttemptId, ExamSchedule examSchedule, User user, Integer duration, Instant startedAt, Optional<ExamRegistration> registration){
        examSchedule.canStart(user.getUserId(),registration,startedAt);
        this.examScheduleId = examSchedule.id();
        this.userId = user.getUserId();
        this.id = examAttemptId;
        this.startedAt = startedAt;
        this.registrationId = registration.map(ExamRegistration::getExamRegistrationId).orElse(null);
        mustFinishedAt = examSchedule.calculateMustFinishedAt(startedAt,duration);
        this.status = ExamStatus.IN_PROGRESS;
    }
    public static ExamAttempt start(ExamAttemptId examAttemptId,ExamSchedule examSchedule, User user, Integer duration,Instant now, Optional<ExamRegistration> registration){
       return new ExamAttempt(examAttemptId,examSchedule, user, duration,now,registration);
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
    public void calculateScore(Exam exam) {
        if (status == ExamStatus.IN_PROGRESS)
            throw new BusinessRuleException("Exam attempt is still in progress");

        int lc = 0, rc = 0;
        int lw = 0, rw = 0;
        int lu = 0, ru = 0;

        for (Part part : exam.getPart()) {
            boolean isListening = part.type().isListening();

            for (QuestionGroup group : part.getQuestionGroups()) {
                for (Question q : group.getQuestions()) {

                    ChoiceKey answered = answers.get(q.id());

                    if (answered == null) {
                        if (isListening) lu++;
                        else ru++;
                    } else if (answered.equals(q.getCorrectChoice())) {
                        if (isListening) lc++;
                        else rc++;
                    } else {
                        if (isListening) lw++;
                        else rw++;
                    }
                }
            }
        }

        this.score = new Score(lc, rc, lw, rw, lu, ru);
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
        if(status!=ExamStatus.IN_PROGRESS) throw new BusinessRuleException("Attempt already submitted");
        if(now.isAfter(mustFinishedAt)){
            expire(now);
            throw new BusinessRuleException("Attempt has expired");
        }
    }
}
