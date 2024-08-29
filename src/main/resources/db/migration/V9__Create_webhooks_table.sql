CREATE TABLE webhooks
(
    id             SERIAL PRIMARY KEY,
    transaction_id INT         NOT NULL,
    request_body   TEXT        NOT NULL,
    response_body  TEXT,
    status         VARCHAR(20) NOT NULL,
    attempt_number INT         NOT NULL DEFAULT 0,
    created_at     TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (transaction_id) REFERENCES transactions (id)
);