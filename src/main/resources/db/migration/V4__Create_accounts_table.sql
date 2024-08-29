CREATE TABLE accounts
(
    id         SERIAL PRIMARY KEY,
    user_id    INT            NOT NULL,
    balance    DECIMAL(19, 4) NOT NULL DEFAULT 0,
    currency   VARCHAR(3)     NOT NULL,
    created_at TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id)
);