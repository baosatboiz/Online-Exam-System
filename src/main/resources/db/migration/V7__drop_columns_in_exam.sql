ALTER TABLE exam
DROP COLUMN open_at,
DROP COLUMN end_at,
DROP COLUMN mode;

ALTER TABLE exam_attempts
ALTER COLUMN status TYPE varchar(50)
USING status::text;