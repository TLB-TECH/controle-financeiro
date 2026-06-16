CREATE TABLE lancamentos (
    id              BIGSERIAL      PRIMARY KEY,
    descricao       VARCHAR(255)   NOT NULL,
    usuario_id      VARCHAR(255)   NOT NULL,
    valor           NUMERIC(15, 2) NOT NULL,
    tipo            VARCHAR(10)    NOT NULL,
    status          VARCHAR(15)    NOT NULL DEFAULT 'PENDENTE',
    data_lancamento DATE           NOT NULL,
    observacao      TEXT,
    ativo           BOOLEAN        NOT NULL DEFAULT TRUE,
    criado_em       TIMESTAMP      NOT NULL DEFAULT NOW(),
    atualizado_em   TIMESTAMP      NOT NULL DEFAULT NOW()
);