ALTER TABLE orcamentos ADD COLUMN tipo VARCHAR(20) NOT NULL DEFAULT 'MENSAL';
ALTER TABLE orcamentos ALTER COLUMN tipo DROP DEFAULT;

ALTER TABLE orcamentos ALTER COLUMN mes DROP NOT NULL;
ALTER TABLE orcamentos ALTER COLUMN ano DROP NOT NULL;

-- Garante no máximo um orçamento definitivo ativo por centro de custo
CREATE UNIQUE INDEX uk_orcamento_centro_definitivo
    ON orcamentos (centro_custo_id)
    WHERE tipo = 'DEFINITIVO' AND ativo = TRUE;
