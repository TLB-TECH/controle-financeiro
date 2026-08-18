-- Orçamentos e Metas de Aplicação passam a ser isolados por usuário (antes eram globais,
-- compartilhados por qualquer login). Backfill: até aqui só existia um usuário real no sistema.

ALTER TABLE orcamentos ADD COLUMN usuario_id VARCHAR(255);
UPDATE orcamentos SET usuario_id = 'taciolb@gmail.com' WHERE usuario_id IS NULL;
ALTER TABLE orcamentos ALTER COLUMN usuario_id SET NOT NULL;

ALTER TABLE orcamentos DROP CONSTRAINT uk_orcamento_centro_mes_ano;
ALTER TABLE orcamentos ADD CONSTRAINT uk_orcamento_centro_mes_ano UNIQUE (usuario_id, centro_custo_id, mes, ano);

DROP INDEX uk_orcamento_centro_definitivo;
CREATE UNIQUE INDEX uk_orcamento_centro_definitivo
    ON orcamentos (usuario_id, centro_custo_id)
    WHERE tipo = 'DEFINITIVO' AND ativo = TRUE;

DROP INDEX uk_orcamento_centro_ano_anual;
CREATE UNIQUE INDEX uk_orcamento_centro_ano_anual
    ON orcamentos (usuario_id, centro_custo_id, ano)
    WHERE tipo = 'ANUAL';

DROP INDEX uk_orcamento_cartao_mes_ano;
CREATE UNIQUE INDEX uk_orcamento_cartao_mes_ano
    ON orcamentos (usuario_id, cartao_credito_id, mes, ano)
    WHERE tipo_alvo = 'CARTAO' AND tipo = 'MENSAL';

DROP INDEX uk_orcamento_cartao_ano_anual;
CREATE UNIQUE INDEX uk_orcamento_cartao_ano_anual
    ON orcamentos (usuario_id, cartao_credito_id, ano)
    WHERE tipo_alvo = 'CARTAO' AND tipo = 'ANUAL';

DROP INDEX uk_orcamento_cartao_definitivo;
CREATE UNIQUE INDEX uk_orcamento_cartao_definitivo
    ON orcamentos (usuario_id, cartao_credito_id)
    WHERE tipo_alvo = 'CARTAO' AND tipo = 'DEFINITIVO' AND ativo = TRUE;

DROP INDEX uk_orcamento_todos_cartoes_mes_ano;
CREATE UNIQUE INDEX uk_orcamento_todos_cartoes_mes_ano
    ON orcamentos (usuario_id, tipo_alvo, mes, ano)
    WHERE tipo_alvo = 'TODOS_CARTOES' AND tipo = 'MENSAL';

DROP INDEX uk_orcamento_todos_cartoes_ano_anual;
CREATE UNIQUE INDEX uk_orcamento_todos_cartoes_ano_anual
    ON orcamentos (usuario_id, tipo_alvo, ano)
    WHERE tipo_alvo = 'TODOS_CARTOES' AND tipo = 'ANUAL';

DROP INDEX uk_orcamento_todos_cartoes_definitivo;
CREATE UNIQUE INDEX uk_orcamento_todos_cartoes_definitivo
    ON orcamentos (usuario_id, tipo_alvo)
    WHERE tipo_alvo = 'TODOS_CARTOES' AND tipo = 'DEFINITIVO' AND ativo = TRUE;

ALTER TABLE metas_aplicacao_mensal ADD COLUMN usuario_id VARCHAR(255);
UPDATE metas_aplicacao_mensal SET usuario_id = 'taciolb@gmail.com' WHERE usuario_id IS NULL;
ALTER TABLE metas_aplicacao_mensal ALTER COLUMN usuario_id SET NOT NULL;
ALTER TABLE metas_aplicacao_mensal DROP CONSTRAINT uk_meta_mensal_mes_ano;
ALTER TABLE metas_aplicacao_mensal ADD CONSTRAINT uk_meta_mensal_mes_ano UNIQUE (usuario_id, mes, ano);

ALTER TABLE metas_aplicacao_anual ADD COLUMN usuario_id VARCHAR(255);
UPDATE metas_aplicacao_anual SET usuario_id = 'taciolb@gmail.com' WHERE usuario_id IS NULL;
ALTER TABLE metas_aplicacao_anual ALTER COLUMN usuario_id SET NOT NULL;
ALTER TABLE metas_aplicacao_anual DROP CONSTRAINT metas_aplicacao_anual_ano_key;
ALTER TABLE metas_aplicacao_anual ADD CONSTRAINT uk_meta_anual_ano UNIQUE (usuario_id, ano);
