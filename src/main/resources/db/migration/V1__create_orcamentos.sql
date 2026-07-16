CREATE TABLE orcamentos (
    id              BIGSERIAL PRIMARY KEY,
    centro_custo_id BIGINT        NOT NULL,
    mes             INTEGER       NOT NULL,
    ano             INTEGER       NOT NULL,
    valor_limite    NUMERIC(15,2) NOT NULL,
    ativo           BOOLEAN       NOT NULL DEFAULT TRUE,
    criado_em       TIMESTAMP     NOT NULL DEFAULT NOW(),

    CONSTRAINT uk_orcamento_centro_mes_ano UNIQUE (centro_custo_id, mes, ano)
);