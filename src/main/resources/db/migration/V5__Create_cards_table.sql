CREATE TABLE cards
(
    id          SERIAL PRIMARY KEY,
    account_id  INT         NOT NULL,
    card_number VARCHAR(16) NOT NULL,
    card_type   VARCHAR(20) NOT NULL,
    exp_date    VARCHAR(5)  NOT NULL,
    cvv         VARCHAR(3)  NOT NULL,
    created_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES accounts (id)
);