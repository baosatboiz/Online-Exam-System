CREATE TYPE exam_status AS ENUM (
    'NOT_STARTED',
    'IN_PROGRESS',
    'SUBMITTED',
    'EXPIRED',
    'PAUSED'
);

CREATE TABLE exam_attempts (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    exam_schedule_id BIGSERIAL NOT NULL,
    started_at TIMESTAMP NOT NULL,
    must_finished_at TIMESTAMP NOT NULL,
    finished_at TIMESTAMP NOT NULL,
    status exam_status NOT NULL,
    CONSTRAINT fk_exam_attempts_schedule FOREIGN KEY (exam_schedule_id) REFERENCES exam_schedules(id)
);