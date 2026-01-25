ALTER TABLE exam
ADD COLUMN business_id UUID;

CREATE EXTENSION IF NOT EXISTS pgcrypto;
UPDATE exam
SET business_id = gen_random_uuid()
WHERE business_id IS NULL;

ALTER TABLE exam
ALTER COLUMN business_id SET NOT NULL;

ALTER TABLE exam
ADD CONSTRAINT uq_exam_business_id UNIQUE (business_id);

ALTER TABLE exam_schedules
ADD COLUMN business_id UUID;

UPDATE exam_schedules
SET business_id = gen_random_uuid()
WHERE business_id IS NULL;

ALTER TABLE exam_schedules
ALTER COLUMN business_id SET NOT NULL;

ALTER TABLE exam_schedules
ADD CONSTRAINT uq_exam_schedule_business_id UNIQUE (business_id);

ALTER TABLE exam_attempts
ADD COLUMN business_id UUID NOT NULL UNIQUE;