ALTER TABLE exam_attempts ADD COLUMN user_id_new BIGINT;

UPDATE exam_attempts SET user_id_new = 15;

ALTER TABLE exam_attempts
    ADD CONSTRAINT fk_exam_attempts_user
        FOREIGN KEY (user_id_new) REFERENCES user_entity(id);

ALTER TABLE exam_attempts DROP COLUMN user_id;

ALTER TABLE exam_attempts RENAME COLUMN user_id_new TO user_id;

ALTER TABLE exam_attempts ALTER COLUMN user_id SET NOT NULL;