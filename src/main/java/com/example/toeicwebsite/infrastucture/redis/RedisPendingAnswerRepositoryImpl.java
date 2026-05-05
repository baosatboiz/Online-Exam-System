package com.example.toeicwebsite.infrastucture.redis;

import com.example.toeicwebsite.application.port.ExamAttemptPendingAnswerPort;
import com.example.toeicwebsite.domain.exam_attempt.model.ExamAttemptId;
import com.example.toeicwebsite.domain.question_bank.model.ChoiceKey;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class RedisPendingAnswerRepositoryImpl implements ExamAttemptPendingAnswerPort {

    private final StringRedisTemplate stringRedisTemplate;

    @Value("${app.redis.stream.exam-answer.key:exam_answer_stream}")
    private String streamKey;

    @Value("${app.redis.hash.exam-attempt.prefix:exam_attempt:}")
    private String hashPrefix;

    @Value("${app.redis.hash.exam-attempt.suffix::answers}")
    private String hashSuffix;

    @Override
    public void savePendingAnswer(ExamAttemptId examAttemptId, Long questionId, ChoiceKey choiceKey) {
        String hashKey = getHashKey(examAttemptId);
        // Save to Redis Hash for fast retrieval
        stringRedisTemplate.opsForHash().put(hashKey, String.valueOf(questionId), choiceKey.name());

        // Push to Redis Stream for async processing
        Map<String, String> eventMap = Map.of(
                "attemptId", examAttemptId.value().toString(),
                "questionId", questionId.toString(),
                "choiceKey", choiceKey.name()
        );
        MapRecord<String, String, String> record = StreamRecords.newRecord()
                .ofMap(eventMap)
                .withStreamKey(streamKey);
        stringRedisTemplate.opsForStream().add(record);
    }

    @Override
    public Map<Long, ChoiceKey> getAndClearPendingAnswers(ExamAttemptId examAttemptId) {
        String hashKey = getHashKey(examAttemptId);
        Map<Object, Object> redisAnswers = stringRedisTemplate.opsForHash().entries(hashKey);
        
        Map<Long, ChoiceKey> pendingAnswers = new HashMap<>();
        for (Map.Entry<Object, Object> entry : redisAnswers.entrySet()) {
            Long questionId = Long.parseLong(entry.getKey().toString());
            ChoiceKey choiceKey = ChoiceKey.valueOf(entry.getValue().toString());
            pendingAnswers.put(questionId, choiceKey);
        }

        // Delete the hash as it's no longer needed
        stringRedisTemplate.delete(hashKey);
        
        return pendingAnswers;
    }
    
    private String getHashKey(ExamAttemptId examAttemptId) {
        return hashPrefix + examAttemptId.value() + hashSuffix;
    }
}
