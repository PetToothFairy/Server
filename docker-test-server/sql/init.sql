CREATE DATABASE IF NOT EXISTS toothFairy;
USE toothFairy;

CREATE TABLE user_tb (
  id BIGINT PRIMARY KEY,
  pet_name VARCHAR(50) NOT NULL,
  pet_weight INT NOT NULL,
  random_id VARCHAR(255) NOT NULL,
  tooth_seq INT NOT NULL DEFAULT 0,
  tooth_date_renew Date NOT NULL,
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;