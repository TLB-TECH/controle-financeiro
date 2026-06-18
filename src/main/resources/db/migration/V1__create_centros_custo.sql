CREATE TABLE centros_custo (
    id           BIGSERIAL PRIMARY KEY,
    nome         VARCHAR(100) NOT NULL,
    descricao    VARCHAR(255),
    ativo        BOOLEAN      NOT NULL DEFAULT TRUE,
    usuario_id   VARCHAR(255) NOT NULL,
    criado_em    TIMESTAMP    NOT NULL,
    atualizado_em TIMESTAMP   NOT NULL
);