ALTER TABLE contas ADD COLUMN tipo_conta_id BIGINT;

ALTER TABLE contas
    ADD CONSTRAINT fk_contas_tipo_conta FOREIGN KEY (tipo_conta_id) REFERENCES tipos_conta(id);

ALTER TABLE contas ALTER COLUMN tipo DROP NOT NULL;
