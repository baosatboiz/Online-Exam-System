CREATE TABLE vocabulary_sets (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE vocabulary_items (
    id UUID PRIMARY KEY,
    set_id UUID NOT NULL,
    user_id UUID NOT NULL,
    term TEXT NOT NULL,
    normalized_term TEXT NOT NULL,
    meaning TEXT NOT NULL,
    note TEXT,
    example TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_vocabulary_items_set FOREIGN KEY (set_id) REFERENCES vocabulary_sets(id) ON DELETE CASCADE,
    CONSTRAINT uq_vocabulary_term_in_set UNIQUE (user_id, set_id, normalized_term)
);

CREATE INDEX idx_vocabulary_sets_user_id ON vocabulary_sets(user_id);
CREATE INDEX idx_vocabulary_items_set_user ON vocabulary_items(set_id, user_id);