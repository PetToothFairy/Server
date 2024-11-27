CREATE DATABASE IF NOT EXISTS toothFairy;
USE toothFairy;

CREATE TABLE user_tb (
  id BIGINT PRIMARY KEY,
  pet_name VARCHAR(50) NOT NULL,
  pet_weight INT NOT NULL,
  random_id VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;