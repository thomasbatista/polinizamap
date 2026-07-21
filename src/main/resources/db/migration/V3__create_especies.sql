CREATE TABLE especies(
    id                 BIGSERIAL PRIMARY KEY,
    nome_popular       VARCHAR(255) NOT NULL,
    nome_cientifico    VARCHAR(255) NOT NULL UNIQUE,
    status_conservacao VARCHAR(30)  NOT NULL
);