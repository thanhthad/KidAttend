CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,

                       password_hash VARCHAR(255) NOT NULL,
                       full_name VARCHAR(100),

                       phone VARCHAR(20) UNIQUE,
                       email VARCHAR(100) UNIQUE,

                       role VARCHAR(20),
                       status VARCHAR(20),

                       created_at TIMESTAMP,
                       updated_at TIMESTAMP
);