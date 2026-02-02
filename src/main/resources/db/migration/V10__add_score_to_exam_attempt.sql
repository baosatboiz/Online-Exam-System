ALTER TABLE exam_attempt_answer
ALTER COLUMN choice_key TYPE VARCHAR(1);

ALTER TABLE exam_attempts
ADD COLUMN listening_score INT DEFAULT 0,
ADD COLUMN reading_score INT DEFAULT 0;