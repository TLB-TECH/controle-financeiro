CREATE TABLE metas_aplicacao_mensal (
    id             BIGSERIAL PRIMARY KEY,
    mes            INT           NOT NULL,
    ano            INT           NOT NULL,
    valor          NUMERIC(15,2) NOT NULL DEFAULT 0,
    atualizado_em  TIMESTAMP     NOT NULL DEFAULT NOW(),
    CONSTRAINT uk_meta_mensal_mes_ano UNIQUE (mes, ano)
);

CREATE TABLE metas_aplicacao_anual (
    id             BIGSERIAL PRIMARY KEY,
    ano            INT           NOT NULL UNIQUE,
    valor          NUMERIC(15,2) NOT NULL DEFAULT 0,
    atualizado_em  TIMESTAMP     NOT NULL DEFAULT NOW()
);

-- Aproveita o valor global existente como ponto de partida do mês/ano de sua última atualização.
INSERT INTO metas_aplicacao_mensal (mes, ano, valor, atualizado_em)
SELECT EXTRACT(MONTH FROM atualizado_em)::int, EXTRACT(YEAR FROM atualizado_em)::int, valor_mensal, atualizado_em
FROM metas_aplicacao
WHERE valor_mensal > 0
ORDER BY id ASC
LIMIT 1;

INSERT INTO metas_aplicacao_anual (ano, valor, atualizado_em)
SELECT EXTRACT(YEAR FROM atualizado_em)::int, valor_anual, atualizado_em
FROM metas_aplicacao
WHERE valor_anual > 0
ORDER BY id ASC
LIMIT 1;

DROP TABLE metas_aplicacao;
