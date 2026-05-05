package com.example.toeicwebsite.infrastucture.db;

import com.example.toeicwebsite.domain.FailedAnswerHandler;
import com.example.toeicwebsite.domain.question_bank.model.ChoiceKey;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class DeadLetterQueueDbImpl implements FailedAnswerHandler {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void pushFailedAnswer(String examAttemptId, Long questionId, ChoiceKey choiceKey) {
        String sql = """
                INSERT INTO dead_letter_queue (exam_attempt_id, question_id, choice_key)
                VALUES (?, ?, ?)
                ON CONFLICT (exam_attempt_id, question_id)
                DO UPDATE SET choice_key = EXCLUDED.choice_key
                """;
        jdbcTemplate.update(sql, examAttemptId, questionId, choiceKey.name());
    }

    @Override
    public Map<Long, ChoiceKey> getFailedAnswers(String examAttemptId) {
        String sql = "SELECT question_id, choice_key FROM dead_letter_queue WHERE exam_attempt_id = ?";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, examAttemptId);

        Map<Long, ChoiceKey> failedAnswers = new HashMap<>();
        for (Map<String, Object> row : rows) {
            Long questionId = ((Number) row.get("question_id")).longValue();
            ChoiceKey choiceKey = ChoiceKey.valueOf((String) row.get("choice_key"));
            failedAnswers.put(questionId, choiceKey);
        }

        return failedAnswers;
    }

    @Override
    public void clearFailedAnswers(String examAttemptId) {
        String sql = "DELETE FROM dead_letter_queue WHERE exam_attempt_id = ?";
        jdbcTemplate.update(sql, examAttemptId);
    }
}