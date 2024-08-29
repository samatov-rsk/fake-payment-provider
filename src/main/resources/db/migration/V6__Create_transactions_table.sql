CREATE TABLE transactions
(
    id                      SERIAL PRIMARY KEY,
    account_from            INT            NOT NULL,
    account_to              INT            NOT NULL,
    amount                  DECIMAL(19, 4) NOT NULL,
    currency                VARCHAR(3)     NOT NULL,
    payment_method          VARCHAR(20)    NOT NULL,
    card_number             VARCHAR(16),
    language                VARCHAR(2)     NOT NULL,
    notification_url        VARCHAR(255)   NOT NULL,
    status                  VARCHAR(20)    NOT NULL,
    message                 VARCHAR(255),
    type                    VARCHAR(20)    NOT NULL,
    external_transaction_id BIGINT,
    customer_first_name     VARCHAR(100)   NOT NULL,
    customer_last_name      VARCHAR(100)   NOT NULL,
    customer_country        VARCHAR(2)     NOT NULL,
    created_at              TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at              TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_from) REFERENCES accounts (id),
    FOREIGN KEY (account_to) REFERENCES accounts (id)
);