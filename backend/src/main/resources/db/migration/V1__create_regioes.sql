CREATE TABLE regioes (
    id     BIGSERIAL    PRIMARY KEY,
    nome   VARCHAR(255) NOT NULL,
    cidade VARCHAR(255) NOT NULL,
    estado CHAR(2)      NOT NULL
);