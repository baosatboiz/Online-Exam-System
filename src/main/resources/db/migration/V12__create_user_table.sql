create table user_entity(
    id BIGSERIAL PRIMARY KEY,
    bussiness_id UUID,
    email VARCHAR(255),
    password VARCHAR(255),
    role TEXT[]
);