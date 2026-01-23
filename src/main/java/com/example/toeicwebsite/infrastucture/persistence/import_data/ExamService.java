package com.example.toeicwebsite.infrastucture.persistence.import_data;

import com.example.toeicwebsite.web.dto.request.ExamRequest;

public interface ExamService {
    void createExam(ExamRequest request);
}
