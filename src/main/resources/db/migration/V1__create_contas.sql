CREATE TABLE contas (
    id         BIGSERIAL PRIMARY KEY,
    nome       VARCHAR(100) NOT NULL,
    banco      VARCHAR(100) NOT NULL,
    tipo       VARCHAR(30)  NOT NULL,
    saldo      NUMERIC(15,2) NOT NULL DEFAULT 0,
    ativo      BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em  TIMESTAMP NOT NULL DEFAULT NOW()
);