package com.example.toeicwebsite.infrastucture.redis;

import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttemptId;
import com.example.toeicwebsite.domain.exam_attempt.repository.ExamAttemptRepository;
import com.example.toeicwebsite.domain.question_bank.model.ChoiceKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnswerStreamConsumer implements StreamListener<String, MapRecord<String, String, String>> {

    private final ExamAttemptRepository examAttemptRepository;
    private final StringRedisTemplate stringRedisTemplate;

    @Value("${app.redis.stream.exam-answer.group:exam_answer_group}")
    private String consumerGroup;

    @Value("${app.redis.stream.exam-answer.key:exam_answer_stream}")
    private String streamKey;

    @Override
    public void onMessage(MapRecord<String, String, String> message) {
        Map<String, String> map = message.getValue();
        try {
            UUID attemptId = UUID.fromString(map.get("attemptId"));
            Long questionId = Long.parseLong(map.get("questionId"));
            ChoiceKey choiceKey = ChoiceKey.valueOf(map.get("choiceKey"));
            
            log.info("Processing answer stream: attempt={}, question={}", attemptId, questionId);
            
            examAttemptRepository.saveAnsweredQuestion(
                    new ExamAttemptId(attemptId),
                    questionId,
                    choiceKey
            );
            
            // Acknowledge the message to the consumer group
            stringRedisTemplate.opsForStream().acknowledge(streamKey, consumerGroup, message.getId());
            
        } catch (Exception e) {
            log.error("Failed to process stream message: {}", map, e);
        }
    }
}
