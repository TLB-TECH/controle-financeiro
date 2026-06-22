CREATE TABLE preferencia_notificacao (
    id BIGSERIAL PRIMARY KEY,
    usuario_id VARCHAR(255) NOT NULL,
    canal VARCHAR(20) NOT NULL,
    telefone VARCHAR(20),
    hora_notificacao INTEGER NOT NULL DEFAULT 8,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em TIMESTAMP NOT NULL,
    atualizado_em TIMESTAMP
);