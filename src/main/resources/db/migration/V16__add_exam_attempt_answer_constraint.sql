ALTER TABLE exam_attempt_answer
ADD CONSTRAINT uq_attempt_question
UNIQUE (attempt_id, question_id);