-- Permite orçamento por cartão de crédito específico ou pra soma de todos os cartões,
-- além do já existente por centro de custo.
ALTER TABLE orcamentos ADD COLUMN tipo_alvo VARCHAR(20) NOT NULL DEFAULT 'CENTRO_CUSTO';
ALTER TABLE orcamentos ALTER COLUMN tipo_alvo DROP DEFAULT;

ALTER TABLE orcamentos ALTER COLUMN centro_custo_id DROP NOT NULL;
ALTER TABLE orcamentos ADD COLUMN cartao_credito_id BIGINT;

-- Únicos por cartão específico (um por período, mesma regra do centro de custo)
CREATE UNIQUE INDEX uk_orcamento_cartao_mes_ano
    ON orcamentos (cartao_credito_id, mes, ano)
    WHERE tipo_alvo = 'CARTAO' AND tipo = 'MENSAL';

CREATE UNIQUE INDEX uk_orcamento_cartao_ano_anual
    ON orcamentos (cartao_credito_id, ano)
    WHERE tipo_alvo = 'CARTAO' AND tipo = 'ANUAL';

CREATE UNIQUE INDEX uk_orcamento_cartao_definitivo
    ON orcamentos (cartao_credito_id)
    WHERE tipo_alvo = 'CARTAO' AND tipo = 'DEFINITIVO' AND ativo = TRUE;

-- Únicos pra "Todos os Cartões" (não há centro_custo_id nem cartao_credito_id nesse caso,
-- então o índice usa tipo_alvo como coluna fixa pra garantir no máximo 1 linha por período)
CREATE UNIQUE INDEX uk_orcamento_todos_cartoes_mes_ano
    ON orcamentos (tipo_alvo, mes, ano)
    WHERE tipo_alvo = 'TODOS_CARTOES' AND tipo = 'MENSAL';

CREATE UNIQUE INDEX uk_orcamento_todos_cartoes_ano_anual
    ON orcamentos (tipo_alvo, ano)
    WHERE tipo_alvo = 'TODOS_CARTOES' AND tipo = 'ANUAL';

CREATE UNIQUE INDEX uk_orcamento_todos_cartoes_definitivo
    ON orcamentos (tipo_alvo)
    WHERE tipo_alvo = 'TODOS_CARTOES' AND tipo = 'DEFINITIVO' AND ativo = TRUE;
