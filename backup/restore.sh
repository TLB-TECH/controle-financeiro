#!/bin/sh
# Restaura um dump num banco. Roda dentro do container backup-financeiro:
#   docker exec backup-financeiro sh /restore.sh contas /backups/contas_20260804_030000.dump
set -e

if [ -z "$1" ] || [ -z "$2" ]; then
  echo "Uso: restore.sh <sufixo-do-banco> <arquivo.dump>"
  echo "Bancos: usuarios contas centrocusto lancamentos orcamento notificacao evolution"
  echo "Ex:  restore.sh contas /backups/contas_20260804_030000.dump"
  exit 1
fi

HOST_SUFFIX="$1"
DUMP_FILE="$2"
HOST="postgres-$HOST_SUFFIX"
export PGPASSWORD="$POSTGRES_PASSWORD"

case "$HOST_SUFFIX" in
  usuarios) DBNAME=CF_usuarios ;;
  contas) DBNAME=CF_contas ;;
  centrocusto) DBNAME=CF_centrocusto ;;
  lancamentos) DBNAME=CF_lancamentos ;;
  orcamento) DBNAME=CF_orcamento ;;
  notificacao) DBNAME=CF_notificacao ;;
  evolution) DBNAME=evolution ;;
  *) echo "Banco desconhecido: $HOST_SUFFIX"; exit 1 ;;
esac

if [ ! -f "$DUMP_FILE" ]; then
  echo "Arquivo não encontrado: $DUMP_FILE"
  exit 1
fi

echo "Restaurando $DUMP_FILE em $HOST/$DBNAME..."
pg_restore -h "$HOST" -U "$POSTGRES_USER" -d "$DBNAME" --clean --if-exists "$DUMP_FILE"
echo "Restauração concluída."
