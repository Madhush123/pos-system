CREATE DATABASE IF NOT EXISTS pos_system_dsmp6;
USE pos_system_dsmp6;
CREATE TABLE IF NOT EXISTS user(
    user_id VARCHAR(80) PRIMARY KEY,
    email VARCHAR(100) UNIQUE,
    display_name VARCHAR(45) NOT NULL,
    contact_number VARCHAR(20) NOT NULL,
    password VARCHAR(255)
);