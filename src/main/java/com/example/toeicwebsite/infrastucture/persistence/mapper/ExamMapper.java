package com.example.toeicwebsite.infrastucture.persistence.mapper;

import com.example.toeicwebsite.domain.exam.model.Exam;
import com.example.toeicwebsite.domain.exam.model.ExamHeader;
import com.example.toeicwebsite.domain.exam.model.ExamId;
import com.example.toeicwebsite.domain.question_bank.model.Question;
import com.example.toeicwebsite.infrastucture.persistence.entity.ExamEntity;
import com.example.toeicwebsite.infrastucture.persistence.entity.QuestionEntity;
import com.example.toeicwebsite.infrastucture.persistence.entity.QuestionGroupEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mapper(
        componentModel = "spring",
        uses = { QuestionGroupMapper.class, QuestionMapper.class }
)
public interface ExamMapper {

    @Mapping(target = "examId", source = "businessId")
    @Mapping(target = "part", source = "questionGroups")
    @Mapping(target = "duration", source = "durationMinutes")
    @Mapping(target = "questionCache", ignore = true)
    Exam toDomain(ExamEntity entity);

    ExamHeader toHeader(ExamEntity entity);

    @Mapping(source = "examId",target = "businessId")
    @Mapping(source = "part",target = "questionGroups")
    @Mapping(source = "duration",target = "durationMinutes")
    ExamEntity toEntity(Exam domain);

    default ExamId map(UUID businessId) {
        return new ExamId(businessId);
    }
    default UUID map(ExamId examId) { return examId.value();}

    @AfterMapping
    default void afterToEntity(@MappingTarget ExamEntity entity, Exam exam) {
        for(QuestionGroupEntity q: entity.getQuestionGroups()) {
            q.setExam(entity);
        }
    }

//    @AfterMapping
//    default void buildQuestionCache(
//            ExamEntity entity,
//            @MappingTarget Exam exam,
//            QuestionMapper questionMapper
//    ) {
//        Map<Integer, Question> cache = new HashMap<>();
//
//        if (entity.getQuestionGroups() != null) {
//            for (QuestionGroupEntity group : entity.getQuestionGroups()) {
//                for (QuestionEntity q : group.getQuestions()) {
//                    cache.put(q.getQuestionNo(), questionMapper.toDomain(q));
//                }
//            }
//        }

//        exam.setQuestionCache(cache);
//    }


}
