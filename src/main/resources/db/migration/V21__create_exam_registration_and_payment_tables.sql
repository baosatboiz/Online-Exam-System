CREATE TABLE IF NOT EXISTS exam_registration (
    exam_registration_id UUID PRIMARY KEY,
    exam_schedule_id UUID NOT NULL,
    user_id UUID NOT NULL,
    registration_status VARCHAR(50) NOT NULL,
    create_at TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    confirm_at TIMESTAMP(6) WITH TIME ZONE,
    expired_at TIMESTAMP(6) WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS payment_order (
    payment_order_id UUID PRIMARY KEY,
    exam_registration_id UUID NOT NULL,
    price NUMERIC(19, 2) NOT NULL,
    payment_status VARCHAR(50) NOT NULL,
    order_code VARCHAR(255) NOT NULL UNIQUE,
    bank_transaction_id VARCHAR(255),
    paid_at TIMESTAMP(6) WITH TIME ZONE,
    created_at TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_payment_order_registration FOREIGN KEY (exam_registration_id) REFERENCES exam_registration(exam_registration_id)
);
