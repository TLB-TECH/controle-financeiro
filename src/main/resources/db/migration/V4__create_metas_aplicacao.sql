CREATE TABLE metas_aplicacao (
    id             BIGSERIAL PRIMARY KEY,
    valor_mensal   NUMERIC(15,2) NOT NULL DEFAULT 0,
    valor_anual    NUMERIC(15,2) NOT NULL DEFAULT 0,
    atualizado_em  TIMESTAMP     NOT NULL DEFAULT NOW()
);
