package com.example.toeicwebsite.infrastucture.redis.event;

import com.example.toeicwebsite.domain.question_bank.model.ChoiceKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmitAnswerEvent {
    private UUID examAttemptId;
    private Long questionId;
    private ChoiceKey choiceKey;
}
