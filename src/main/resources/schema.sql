-- schema.sql
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS landings (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    template_id VARCHAR(255),
    user_id BIGINT REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS blocks (
    id BIGSERIAL PRIMARY KEY,
    block_id VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    content TEXT,
    "order" INTEGER NOT NULL,
    landing_id BIGINT REFERENCES landings(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS templates (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    author VARCHAR(255),
    license VARCHAR(255),
    price DECIMAL(10,2),
    preview_image TEXT
);

CREATE TABLE IF NOT EXISTS template_keywords (
    template_id VARCHAR(255) REFERENCES templates(id) ON DELETE CASCADE,
    keyword VARCHAR(255),
    PRIMARY KEY (template_id, keyword)
);

CREATE TABLE IF NOT EXISTS template_blocks (
    id BIGSERIAL PRIMARY KEY,
    block_id VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    label VARCHAR(255),
    default_value TEXT,
    required BOOLEAN DEFAULT FALSE,
    "order" INTEGER NOT NULL,
    template_id VARCHAR(255) REFERENCES templates(id) ON DELETE CASCADE
);