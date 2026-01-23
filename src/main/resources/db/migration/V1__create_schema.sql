CREATE TABLE exam (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255),
    description TEXT,
    duration_minutes INT NOT NULL,             -- 120
    total_questions INT NOT NULL,               -- 200
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE question_group (
    id BIGSERIAL PRIMARY KEY,
    exam_id BIGINT NOT NULL,
    part INT NOT NULL CHECK (part BETWEEN 1 AND 7),
    group_no INT NOT NULL,                      -- số thứ tự trong part
    audio_url TEXT,
    image_url TEXT,
    passage_text TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_qg_exam
        FOREIGN KEY (exam_id)
            REFERENCES exam(id)
            ON DELETE CASCADE
);

CREATE TABLE question (
    id BIGSERIAL PRIMARY KEY,
    group_id BIGINT NOT NULL,
    question_no INT NOT NULL,                   -- 1..200 (toàn đề)
    content TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_question_group
      FOREIGN KEY (group_id)
          REFERENCES question_group(id)
          ON DELETE CASCADE
);

CREATE TABLE choice (
    id BIGSERIAL PRIMARY KEY,
    question_id BIGINT NOT NULL,
    label VARCHAR(1) NOT NULL CHECK (label IN ('A','B','C','D')),
    content TEXT,
    is_correct BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_choice_question
        FOREIGN KEY (question_id)
            REFERENCES question(id)
            ON DELETE CASCADE,

    CONSTRAINT uq_choice_label
        UNIQUE (question_id, label)
);