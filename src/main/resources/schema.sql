-- schema.sql
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(20) NOT NULL,
    password VARCHAR(200) NOT NULL,
    roles VARCHAR(255),
    enabled boolean,
    account_non_expired boolean,
    account_non_locked boolean,
    credentials_non_expired boolean
);