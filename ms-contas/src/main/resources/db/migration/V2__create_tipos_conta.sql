CREATE TABLE tipos_conta (
    id            BIGSERIAL PRIMARY KEY,
    nome          VARCHAR(100) NOT NULL,
    categoria     VARCHAR(20)  NOT NULL,
    ativo         BOOLEAN NOT NULL DEFAULT TRUE,
    usuario_id    VARCHAR(255) NOT NULL,
    criado_em     TIMESTAMP NOT NULL,
    atualizado_em TIMESTAMP NOT NULL
);
