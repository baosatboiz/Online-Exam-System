CREATE TYPE exam_mode AS ENUM (
    'MINI_TEST',
    'PRACTICE',
    'REAL'
);

ALTER TABLE exam
ADD COLUMN open_at TIMESTAMPTZ,
ADD COLUMN end_at TIMESTAMPTZ,
ADD COLUMN mode exam_mode;

UPDATE exam
SET
    open_at = created_at,
    end_at = created_at + INTERVAL '120 minutes',
    mode = 'PRACTICE'
WHERE open_at IS NULL;

ALTER TABLE exam
ALTER COLUMN open_at SET NOT NULL,
ALTER COLUMN end_at SET NOT NULL,
ALTER COLUMN mode SET NOT NULL;

ALTER TABLE question
ADD COLUMN explanation TEXT;