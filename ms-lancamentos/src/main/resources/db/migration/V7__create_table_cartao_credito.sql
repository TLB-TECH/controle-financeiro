CREATE TABLE cartao_credito (
    id            BIGSERIAL PRIMARY KEY,
    nome          VARCHAR(100)   NOT NULL,
    limite        NUMERIC(15, 2),
    usuario_id    VARCHAR(255)   NOT NULL,
    ativo         BOOLEAN        NOT NULL DEFAULT TRUE,
    criado_em     TIMESTAMP      NOT NULL,
    atualizado_em TIMESTAMP      NOT NULL
);