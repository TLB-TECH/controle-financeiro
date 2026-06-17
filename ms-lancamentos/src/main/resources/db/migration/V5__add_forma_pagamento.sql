ALTER TABLE lancamentos
    ADD COLUMN forma_pagamento VARCHAR(20),
    ADD COLUMN numero_parcelas INTEGER DEFAULT 1;