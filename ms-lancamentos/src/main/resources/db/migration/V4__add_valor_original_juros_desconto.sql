ALTER TABLE lancamentos
    ADD COLUMN valor_original NUMERIC(15, 2),
    ADD COLUMN taxa_juros     NUMERIC(5, 2)  DEFAULT 0,
    ADD COLUMN tipo_juros     VARCHAR(10),
    ADD COLUMN juros          NUMERIC(15, 2) DEFAULT 0,
    ADD COLUMN desconto       NUMERIC(15, 2) DEFAULT 0;

UPDATE lancamentos
SET valor_original = valor,
    taxa_juros     = 0,
    juros          = 0,
    desconto       = 0;

ALTER TABLE lancamentos
    ALTER COLUMN valor_original SET NOT NULL;