-- Refactor exam_registration: add Long auto-inc PK + rename UUID to business_id
ALTER TABLE payment_order DROP CONSTRAINT IF EXISTS fk_payment_order_registration;

ALTER TABLE exam_registration DROP CONSTRAINT exam_registration_pkey;
ALTER TABLE exam_registration RENAME COLUMN exam_registration_id TO business_id;
ALTER TABLE exam_registration ADD COLUMN id BIGSERIAL;
ALTER TABLE exam_registration ADD PRIMARY KEY (id);
ALTER TABLE exam_registration ADD CONSTRAINT uk_exam_registration_business_id UNIQUE (business_id);

-- Refactor payment_order: add Long auto-inc PK + rename UUID to business_id
ALTER TABLE payment_order DROP CONSTRAINT payment_order_pkey;
ALTER TABLE payment_order RENAME COLUMN payment_order_id TO business_id;
ALTER TABLE payment_order ADD COLUMN id BIGSERIAL;
ALTER TABLE payment_order ADD PRIMARY KEY (id);
ALTER TABLE payment_order ADD CONSTRAINT uk_payment_order_business_id UNIQUE (business_id);

-- Re-add FK (now references business_id which has a unique constraint)
ALTER TABLE payment_order ADD CONSTRAINT fk_payment_order_registration
    FOREIGN KEY (exam_registration_id) REFERENCES exam_registration(business_id);
