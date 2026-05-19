ALTER TABLE exam_attempts ADD COLUMN version BIGINT DEFAULT 0;
UPDATE exam_attempts SET version = 0 WHERE version IS NULL;
ALTER TABLE exam_attempts ALTER COLUMN version SET NOT NULL;
