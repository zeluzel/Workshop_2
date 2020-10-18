# Zapytania do warsztatu

# tworzenie bazy danych i tabeli users
CREATE DATABASE workshop2
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
CREATE TABLE users
(
    id       INT AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL UNIQUE,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(60)  NOT NULL,
    PRIMARY KEY (id)
);

# dodawanie użytkownika
INSERT INTO users(email, username, password)
VALUES (?, ?, ?);

# zmiana danych
# do przemyślenia!
UPDATE users
SET
    username = ?,
    email = ?,
    password = ?
WHERE id = ?;

# pobieranie po id
SELECT * FROM users
WHERE id = ?;

# usuwanie po id
DELETE FROM users
WHERE id = ?;
DELETE FROM users WHERE id = ?;

# pobieranie wszystkich użytkowników
SELECT * FROM users;