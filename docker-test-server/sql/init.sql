CREATE DATABASE IF NOT EXISTS toothFairy;
USE toothFairy;

CREATE TABLE user_tb (
  email VARCHAR(255) PRIMARY KEY,
  pet_name VARCHAR(50),
  pet_weight INT,
  access_token VARCHAR(255),
  refresh_token VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO user_tb (email, pet_name, pet_weight, access_token, refresh_token)
VALUES ('test@gmail.com', 'Charlie', 12, 'test_access_token', 'test_refresh_token');