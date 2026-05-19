package com.example.toeicwebsite.infrastucture.persistence.jdbc_repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ExamAttemptAnswerJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public void batchUpsertAnswers(Long attemptId, Map<Long, String> answers) {
        if (answers == null || answers.isEmpty()) {
            return;
        }

        String sql = """
            INSERT INTO exam_attempt_answer (attempt_id, question_id, choice_key)
            VALUES (?, ?, ?)
            ON CONFLICT (attempt_id, question_id)
            DO UPDATE SET choice_key = EXCLUDED.choice_key
        """;

        List<Map.Entry<Long, String>> entries = answers.entrySet().stream().toList();

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Map.Entry<Long, String> entry = entries.get(i);
                ps.setLong(1, attemptId);
                ps.setLong(2, entry.getKey());
                ps.setString(3, entry.getValue());
            }

            @Override
            public int getBatchSize() {
                return entries.size();
            }
        });
    }
}
