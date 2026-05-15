CREATE TABLE user_ai_config (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    provider VARCHAR(50) NOT NULL,
    api_key_encrypted TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    UNIQUE(user_id, provider)
);

CREATE INDEX idx_user_ai_config_user_id ON user_ai_config(user_id);