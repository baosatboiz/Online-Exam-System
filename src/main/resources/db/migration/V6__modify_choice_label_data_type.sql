CREATE TYPE choice_key AS ENUM (
    'A',
    'B',
    'C',
    'D'
);

ALTER TABLE choice
ALTER COLUMN label TYPE choice_key
USING label::choice_key;