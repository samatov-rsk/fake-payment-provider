CREATE TABLE users
(
    id         SERIAL PRIMARY KEY,
    user_type  VARCHAR(20) NOT NULL,
    created_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);