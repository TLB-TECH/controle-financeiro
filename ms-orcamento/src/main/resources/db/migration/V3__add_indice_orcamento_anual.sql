CREATE UNIQUE INDEX uk_orcamento_centro_ano_anual
    ON orcamentos (centro_custo_id, ano)
    WHERE tipo = 'ANUAL';
