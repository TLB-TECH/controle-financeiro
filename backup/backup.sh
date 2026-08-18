#!/bin/sh
# Faz um dump de cada banco Postgres do sistema e apaga backups com mais de 14 dias.
# Rodado uma vez ao subir o container (backup imediato) e depois todo dia às 3h via cron.
set -e

BACKUP_DIR=/backups
TIMESTAMP=$(date +%Y%m%d_%H%M%S)
export PGPASSWORD="$POSTGRES_PASSWORD"

mkdir -p "$BACKUP_DIR"

# formato "sufixo-do-host:nome-do-banco"
DBS="usuarios:CF_usuarios contas:CF_contas centrocusto:CF_centrocusto lancamentos:CF_lancamentos orcamento:CF_orcamento notificacao:CF_notificacao evolution:evolution"

FALHAS=0
for ENTRY in $DBS; do
  HOST_SUFFIX="${ENTRY%%:*}"
  DBNAME="${ENTRY##*:}"
  HOST="postgres-$HOST_SUFFIX"
  OUT="$BACKUP_DIR/${HOST_SUFFIX}_${TIMESTAMP}.dump"

  if pg_dump -h "$HOST" -U "$POSTGRES_USER" -d "$DBNAME" -F c -f "$OUT"; then
    echo "$(date -Iseconds) OK   $HOST_SUFFIX -> $OUT"
  else
    echo "$(date -Iseconds) FALHOU $HOST_SUFFIX" >&2
    FALHAS=$((FALHAS + 1))
  fi
done

# Retenção: mantém só os últimos 14 dias
find "$BACKUP_DIR" -name "*.dump" -mtime +14 -delete

if [ "$FALHAS" -gt 0 ]; then
  echo "$(date -Iseconds) Backup terminou com $FALHAS falha(s)" >&2
  exit 1
fi
