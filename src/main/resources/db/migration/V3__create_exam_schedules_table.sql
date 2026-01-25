CREATE TABLE exam_schedules (
    id BIGSERIAL PRIMARY KEY,
    exam_id BIGSERIAL NOT NULL,
    open_at TIMESTAMP NOT NULL,
    end_at TIMESTAMP NOT NULL,
    mode exam_mode,
    CONSTRAINT fk_exam_schedules_exam FOREIGN KEY (exam_id) REFERENCES exam(id)
);