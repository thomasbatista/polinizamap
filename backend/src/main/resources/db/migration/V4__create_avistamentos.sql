CREATE TABLE avistamentos (
    id             BIGSERIAL        PRIMARY KEY,
    usuario_id     BIGINT           NOT NULL REFERENCES usuarios (id),
    especie_id     BIGINT           NOT NULL REFERENCES especies (id),
    regiao_id      BIGINT           NOT NULL REFERENCES regioes (id),
    latitude       DOUBLE PRECISION NOT NULL,
    longitude      DOUBLE PRECISION NOT NULL,
    descricao      TEXT,
    data_hora      TIMESTAMP        NOT NULL,
    status         VARCHAR(20)      NOT NULL DEFAULT 'PENDENTE',
    nota_validacao TEXT
);