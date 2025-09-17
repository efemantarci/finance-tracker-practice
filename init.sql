DROP DATABASE finance_tracker;
CREATE DATABASE finance_tracker;
USE finance_tracker;

CREATE TABLE users (
    username VARCHAR(255),
    password_hash VARCHAR(255) NOT NULL,
    budget DECIMAL(10,2) DEFAULT 0.00,
    PRIMARY KEY(username)
);

CREATE TABLE transaction_categories (
    category_id INT AUTO_INCREMENT,
    category_name VARCHAR(255) NOT NULL,
    PRIMARY KEY (category_id)
);

CREATE TABLE transactions (
    transaction_id INT AUTO_INCREMENT,
    user VARCHAR(255) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    category_id INT NOT NULL,
    transaction_type ENUM("income","expense") NOT NULL,
    transaction_date DATE NOT NULL,
    description VARCHAR(500),
    PRIMARY KEY (transaction_id),
    FOREIGN KEY (user) REFERENCES users(username),
    FOREIGN KEY (category_id) REFERENCES transaction_categories(category_id)
);

-- Insert dummy data
INSERT INTO users(username,password_hash) VALUES ("dummy","test");