package com.example.toeicwebsite.service.impl;

import com.example.toeicwebsite.dto.request.ChoiceRequest;
import com.example.toeicwebsite.dto.request.ExamRequest;
import com.example.toeicwebsite.dto.request.QuestionGroupRequest;
import com.example.toeicwebsite.dto.request.QuestionRequest;
import com.example.toeicwebsite.entity.ChoiceEntity;
import com.example.toeicwebsite.entity.ExamEntity;
import com.example.toeicwebsite.entity.QuestionEntity;
import com.example.toeicwebsite.entity.QuestionGroupEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.toeicwebsite.repository.ExamRepository;
import com.example.toeicwebsite.service.ExamService;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {

    private final ExamRepository examRepository;

    @Override
    public void createExam(ExamRequest request) {
        ExamEntity exam = mapExam(request);

        if (request.getQuestionGroups() != null) {
            exam.setQuestionGroups(request.getQuestionGroups().stream()
                            .map(gReq -> mapGroup(gReq, exam))
                            .toList());
        }
        examRepository.save(exam);
    }

    private ExamEntity mapExam(ExamRequest request) {
        ExamEntity exam = new ExamEntity();
        exam.setTitle(request.getTitle());
        exam.setDescription(request.getDescription());
        exam.setDurationMinutes(request.getDurationMinutes());
        exam.setTotalQuestions(request.getTotalQuestions());
        return exam;
    }

    private QuestionGroupEntity mapGroup(QuestionGroupRequest gReq, ExamEntity exam) {
        QuestionGroupEntity group = new QuestionGroupEntity();
        group.setPart(gReq.getPart());
        group.setGroupNo(gReq.getGroupNo());
        group.setAudioUrl(gReq.getAudioUrl());
        group.setImageUrl(gReq.getImageUrl());
        group.setPassageText(gReq.getPassageText());
        group.setExam(exam);

        if (gReq.getQuestions() != null) {
            group.setQuestions(gReq.getQuestions().stream()
                            .map(qReq -> mapQuestion(qReq, group))
                            .toList());
        }
        return group;
    }

    private QuestionEntity mapQuestion(QuestionRequest qReq, QuestionGroupEntity group) {
        QuestionEntity question = new QuestionEntity();
        question.setQuestionNo(qReq.getQuestionNo());
        question.setContent(qReq.getContent());
        question.setGroup(group);

        if (qReq.getChoices() != null) {
            question.setChoices(qReq.getChoices().stream()
                            .map(cReq -> mapChoice(cReq, question))
                            .toList());
        }
        return question;
    }

    private ChoiceEntity mapChoice(ChoiceRequest cReq, QuestionEntity question) {
        ChoiceEntity choice = new ChoiceEntity();
        choice.setLabel(cReq.getLabel());
        choice.setContent(cReq.getContent());
        choice.setIsCorrect(cReq.getIsCorrect());
        choice.setQuestion(question);
        return choice;
    }
}
