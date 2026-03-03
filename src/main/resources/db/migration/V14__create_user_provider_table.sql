alter table user_entity
    add constraint uk_bussiness_id unique(bussiness_id);

CREATE TABLE user_provider (
   id BIGSERIAL PRIMARY KEY,
   user_business_id UUID NOT NULL,
   provider VARCHAR(50) NOT NULL,
   provider_id VARCHAR(255),
   CONSTRAINT fk_user_provider_user
       FOREIGN KEY (user_business_id) REFERENCES user_entity(bussiness_id) ON DELETE CASCADE,
   CONSTRAINT uq_provider_id
       UNIQUE (provider, provider_id)
);

-- Migrate tất cả user hiện tại sang LOCAL provider
INSERT INTO user_provider (user_business_id, provider, provider_id)
SELECT bussiness_id, 'LOCAL', NULL
FROM user_entity;