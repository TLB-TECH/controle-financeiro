ALTER TABLE lancamentos
    ADD COLUMN cartao_credito_id BIGINT REFERENCES cartao_credito(id),
    ADD COLUMN parcela_numero    INTEGER,
    ADD COLUMN grupo_parcela_id  VARCHAR(36);