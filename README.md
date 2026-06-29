# Controle Financeiro Pessoal

Sistema de controle financeiro pessoal construído com arquitetura de microsserviços. Permite gerenciar lançamentos, centros de custo, fluxo de caixa e receber notificações por e-mail e WhatsApp sobre vencimentos.

## Arquitetura

```
                        [ Frontend Angular ]
                               |
                        [ API Gateway :8080 ]
                      /    |      |      \
              ms-usuarios  ms-lancamentos  ms-centro-custo  ms-fluxo-caixa
               :8081          :8083            :8082            :8084
                                |
                         bff-financeiro
                             :8085
                                |
                         ms-notificacao
                             :8086
                                |
                         Evolution API (WhatsApp)
                             :8089
```

## Repositórios

| Repositório | Descrição |
|---|---|
| [controle-financeiro](https://github.com/TLB-TECH/controle-financeiro) | Monorepo (ms-lancamentos, ms-usuarios) |
| [ms-api-gateway](https://github.com/TLB-TECH/ms-api-gateway) | API Gateway |
| [bff-financeiro](https://github.com/TLB-TECH/bff-financeiro) | Backend for Frontend |
| [ms-centro-custo](https://github.com/TLB-TECH/ms-centro-custo) | Microsserviço de centros de custo |
| [ms-fluxo-caixa](https://github.com/TLB-TECH/ms-fluxo-caixa) | Microsserviço de fluxo de caixa |
| [ms-notificacao](https://github.com/TLB-TECH/ms-notificacao) | Microsserviço de notificações |
| [controle-financeiro-infra](https://github.com/TLB-TECH/controle-financeiro-infra) | Infraestrutura Docker |
| [controle-financeiro-usuario-front](https://github.com/TLB-TECH/controle-financeiro-usuario-front) | Frontend Angular |

## Microsserviços

### API Gateway — porta 8080
Ponto de entrada único da aplicação. Roteia as requisições para os microsserviços e valida o token JWT.

| Rota | Destino |
|---|---|
| `/auth/**`, `/usuarios/**` | ms-usuarios |
| `/lancamentos/**` | ms-lancamentos |
| `/centros-custo/**` | ms-centro-custo |
| `/fluxo-caixa/**` | ms-fluxo-caixa |

### ms-usuarios — porta 8081
Gerencia autenticação e cadastro de usuários. Emite tokens JWT usados por todos os demais serviços.

**Endpoints:**
- `POST /auth/login` — autenticação
- `POST /usuarios` — cadastro de usuário
- `GET /usuarios/{id}` — busca usuário

### ms-centro-custo — porta 8082
Gerencia os centros de custo vinculados ao usuário.

**Endpoints:**
- `POST /centros-custo`
- `GET /centros-custo`
- `PUT /centros-custo/{id}`
- `DELETE /centros-custo/{id}`

### ms-lancamentos — porta 8083
Principal microsserviço do sistema. Gerencia lançamentos financeiros com suporte a parcelamentos e cartão de crédito.

**Funcionalidades:**
- Lançamentos de receita e despesa
- Formas de pagamento: cartão de crédito, boleto, dinheiro, PIX, transferência
- Lançamentos parcelados (boleto e cartão)
- Efetivação de lançamentos pendentes
- Tipos de juros configuráveis
- Endpoint interno para notificações de vencimento

**Endpoints:**
- `POST /lancamentos` — criar lançamento
- `GET /lancamentos` — listar lançamentos do usuário
- `PUT /lancamentos/{id}` — atualizar
- `DELETE /lancamentos/{id}` — excluir
- `POST /lancamentos/{id}/efetivar` — efetivar lançamento
- `POST /lancamentos/parcelados` — criar lançamento parcelado
- `POST /cartoes` — cadastrar cartão de crédito
- `GET /cartoes` — listar cartões

### ms-fluxo-caixa — porta 8084
Consolida os lançamentos e apresenta o fluxo de caixa por período.

**Endpoints:**
- `GET /fluxo-caixa` — fluxo de caixa por período

### bff-financeiro — porta 8085
Backend for Frontend. Agrega dados de múltiplos microsserviços em uma única chamada para o frontend.

**Endpoints:**
- `GET /bff/dashboard` — retorna lançamentos, centros de custo e fluxo de caixa consolidados

### ms-notificacao — porta 8086
Envia notificações de lançamentos vencendo por e-mail (SendGrid) e WhatsApp (Evolution API). Possui scheduler que verifica diariamente os vencimentos.

**Funcionalidades:**
- Notificação por e-mail via SendGrid
- Notificação por WhatsApp via Evolution API (ativação via flag `whatsapp.enabled`)
- Preferências de notificação por usuário (e-mail, WhatsApp ou ambos)
- Scheduler automático de alertas de vencimento

**Endpoints:**
- `POST /notificacoes/preferencias` — configurar preferências
- `GET /notificacoes/preferencias` — consultar preferências

## Frontend — Angular

Aplicação Angular com as seguintes telas:

| Tela | Rota |
|---|---|
| Login | `/login` |
| Cadastro | `/register` |
| Dashboard | `/dashboard` |
| Lançamentos | `/lancamentos` |
| Centro de Custo | `/centro-custo` |

Guards de autenticação e interceptor HTTP para envio automático do token JWT.

## Infraestrutura

Toda a infraestrutura está definida no repositório [controle-financeiro-infra](https://github.com/TLB-TECH/controle-financeiro-infra) via Docker Compose.

### Bancos de dados (PostgreSQL 15)

| Container | Banco | Porta |
|---|---|---|
| postgres-usuarios | CF_usuarios | 5433 |
| postgres-centrocusto | CF_centrocusto | 5434 |
| postgres-lancamentos | CF_lancamentos | 5435 |
| postgres-notificacao | CF_notificacao | 5436 |
| postgres-evolution | evolution | 5437 |

### Como subir o ambiente

**1. Clone a infraestrutura:**
```bash
git clone https://github.com/TLB-TECH/controle-financeiro-infra.git
cd controle-financeiro-infra
```

**2. Configure as variáveis de ambiente:**
```bash
cp .env.example .env
# Edite o .env com suas credenciais
```

**3. Suba os containers:**
```bash
docker-compose up -d
```

### Variáveis de ambiente (.env)

```env
POSTGRES_USER=postgres
POSTGRES_PASSWORD=sua_senha

JWT_SECRET=seu_jwt_secret
JWT_EXPIRATION=86400000

INTERNAL_SECRET=internal-secret-financeiro-2026

# SendGrid (e-mail)
SENDGRID_API_KEY=sua_chave
SENDGRID_FROM_EMAIL=seu@email.com

# Evolution API (WhatsApp)
EVOLUTION_API_URL=http://evolution-api:8080
EVOLUTION_API_KEY=sua_chave
EVOLUTION_INSTANCE=sua_instancia
```

## Tecnologias

**Backend:**
- Java 17 + Spring Boot 3
- Spring Cloud Gateway
- Spring Security + JWT
- Spring Data JPA + Flyway
- PostgreSQL 15
- OpenFeign (comunicação entre microsserviços)
- SpringDoc (Swagger)

**Frontend:**
- Angular 17+
- TypeScript

**Infraestrutura:**
- Docker + Docker Compose
- Evolution API v2 (WhatsApp)
- SendGrid (e-mail)

## Documentação da API

Cada microsserviço expõe o Swagger UI em:
```
http://localhost:{porta}/swagger-ui.html
```
