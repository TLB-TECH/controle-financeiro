ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS status_assinatura VARCHAR(20) NOT NULL DEFAULT 'TRIAL';
ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS trial_fim TIMESTAMP;
ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS mp_preapproval_id VARCHAR(100);
ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS assinatura_atualizada_em TIMESTAMP;

UPDATE usuarios SET trial_fim = data_criacao + INTERVAL '7 days' WHERE trial_fim IS NULL;

ALTER TABLE usuarios ALTER COLUMN trial_fim SET NOT NULL;

COMMENT ON COLUMN usuarios.status_assinatura IS 'TRIAL, ATIVO, INADIMPLENTE ou CANCELADO';
COMMENT ON COLUMN usuarios.trial_fim IS 'Data/hora em que o trial gratuito de 7 dias termina';
COMMENT ON COLUMN usuarios.mp_preapproval_id IS 'Id da assinatura (preapproval) no Mercado Pago';
COMMENT ON COLUMN usuarios.assinatura_atualizada_em IS 'Ultima vez que o status da assinatura foi atualizado via webhook';
