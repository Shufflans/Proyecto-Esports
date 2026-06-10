CREATE TABLE usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL
);

CREATE TABLE refresh_token (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(255) NOT NULL,
    username VARCHAR(55) NOT NULL,
    expiry_date DATETIME NOT NULL
);

INSERT INTO usuario (username, password, role) VALUES (
    'admin',
    '$2a$12$jU.GCWqrbyB7dh.MEpoF5OEDhTgHLzS4ZH3HIMmavcbqEoDNt2CJa',
    'ROLE_ADMIN'
),(
    'usuario',
    '$2a$12$Ej00Il93ygEci4JPlDcmyO.tM5llgEVyXHfG1gL0TBEyI4FsnSxla',
    'ROLE_USER'
);