CREATE TABLE dead_letter_queue (
    id          BIGSERIAL PRIMARY KEY,
    exam_attempt_id UUID        NOT NULL,
    question_id     BIGINT      NOT NULL,
    choice_key      VARCHAR(5)  NOT NULL,
    created_at      TIMESTAMP   NOT NULL DEFAULT now(),
    UNIQUE (exam_attempt_id, question_id)
);

CREATE INDEX idx_dlq_exam_attempt_id ON dead_letter_queue (exam_attempt_id);
