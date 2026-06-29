# 💰 Controle Financeiro Pessoal

> Aplicação de controle financeiro pessoal desenvolvida com arquitetura de microsserviços, utilizando Java + Spring Boot, Angular e Docker.

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?style=flat-square&logo=springboot)
![Angular](https://img.shields.io/badge/Angular-20-red?style=flat-square&logo=angular)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?style=flat-square&logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=flat-square&logo=docker)
![Status](https://img.shields.io/badge/Status-Em%20desenvolvimento-yellow?style=flat-square)

---

## 📋 Sobre o Projeto

Aplicação full-stack para controle de finanças pessoais, com foco em **aprendizado de arquitetura distribuída** e construção de **portfólio profissional**.

O projeto foi desenvolvido seguindo boas práticas de mercado: microsserviços independentes, comunicação via REST, autenticação JWT centralizada no API Gateway, resiliência com Resilience4j e orquestração com Docker Compose. Conta ainda com notificações automáticas de vencimentos por **e-mail** e **WhatsApp**.

---

## 🏗️ Arquitetura

```
┌─────────────────────────────────────────────────────┐
│              Angular Frontend (4200)                 │
└──────────────────────┬──────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────┐
│         API Gateway — Spring Cloud Gateway (8080)    │
│              JWT Validation · Roteamento             │
└──┬──────────┬──────────────┬───────────────┬────────┘
   │          │              │               │
┌──▼───┐ ┌───▼──┐      ┌───▼──┐      ┌─────▼─────┐
│  ms- │ │  ms- │      │  ms- │      │    ms-    │
│usu.  │ │c.c.  │      │fluxo │      │lanc.(8083)│
│(8081)│ │(8082)│      │(8084)│      └─────┬─────┘
└──────┘ └──────┘      └──────┘            │
db_usu.  db_c.c.       db_fluxo            │
                                    ┌──────▼──────┐
                                    │ ms-notif.   │
┌───────────────────────────────┐   │   (8086)    │
│      bff-financeiro (8085)    │   │ Email/Whats │
│  Agrega: lanc. + c.c. + fluxo│   └──────┬──────┘
└───────────────────────────────┘   db_notif.    │
                                               ┌──▼──────────┐
                                               │ Evolution   │
                                               │ API (8089)  │
                                               └─────────────┘
```

### Por que essa arquitetura?

| Decisão | Motivo |
|---|---|
| Microsserviços | Escalabilidade independente por domínio |
| API Gateway | Ponto único de entrada, JWT centralizado |
| BFF | Agrega dados complexos para o frontend |
| Resilience4j | Evita falhas em cascata entre serviços |
| Um banco por serviço | Isolamento de dados, sem acoplamento |
| Evolution API | Integração WhatsApp sem custo via API aberta |

---

## 🛠️ Stack Tecnológica

| Camada | Tecnologia |
|---|---|
| Backend | Java 17 + Spring Boot 3.x |
| API Gateway | Spring Cloud Gateway |
| Comunicação | REST + OpenFeign |
| Resiliência | Resilience4j (Circuit Breaker, Retry, Timeout) |
| Autenticação | JWT + Spring Security |
| Banco de Dados | PostgreSQL 15 |
| Frontend | Angular 20 |
| Orquestração | Docker + Docker Compose |
| Documentação | Swagger / OpenAPI 3 |
| Testes | JUnit 5 + Mockito |
| E-mail | SendGrid |
| WhatsApp | Evolution API v2 |
| CI/CD | GitHub Actions (em breve) |

---

## 📦 Estrutura do Projeto

```
controle-financeiro/
├── controle-financeiro-infra/     # Docker Compose + infra
├── api-gateway/                   # Spring Cloud Gateway — porta 8080
├── ms-usuarios/                   # Microsserviço de Usuários — porta 8081
├── ms-centro-custo/               # Microsserviço de Centro de Custo — porta 8082
├── ms-lancamentos/                # Microsserviço de Lançamentos — porta 8083
├── ms-fluxo-caixa/                # Microsserviço de Fluxo de Caixa — porta 8084
├── bff-financeiro/                # Backend for Frontend — porta 8085
├── ms-notificacao/                # Microsserviço de Notificações — porta 8086
└── CotroleFinanceiroFront/        # Aplicação Angular — porta 4200
    └── controle-financeiro-front/
```

---

## 📋 Módulos

### 👤 ms-usuarios (8081)
- Cadastro e autenticação de usuários
- Geração e validação de tokens JWT
- CRUD completo de usuários

### 🗂️ ms-centro-custo (8082)
- Cadastro de centros de custo
- CRUD completo
- Vinculação com lançamentos

### 💸 ms-lancamentos (8083)
- Registro de receitas e despesas com múltiplas formas de pagamento (PIX, boleto, cartão, dinheiro, transferência)
- Lançamentos parcelados (boleto e cartão de crédito)
- Cadastro e gerenciamento de cartões de crédito
- Efetivação de lançamentos pendentes
- Tipos de juros configuráveis
- Vinculação com centros de custo via OpenFeign

### 📊 ms-fluxo-caixa (8084)
- Relatórios financeiros consolidados
- Extrato por período
- Saldo atual e projeções
- Consome `ms-lancamentos` via OpenFeign + Resilience4j

### 🖥️ bff-financeiro (8085)
- Agrega em uma única chamada: lançamentos, centros de custo e fluxo de caixa
- Simplifica o consumo de dados pelo frontend (dashboard)

### 🔔 ms-notificacao (8086)
- Alertas automáticos de lançamentos vencendo (scheduler diário)
- Notificação por **e-mail** via SendGrid
- Notificação por **WhatsApp** via Evolution API (flag `whatsapp.enabled`)
- Preferências de notificação por usuário (e-mail, WhatsApp ou ambos)

---

## 🌐 Frontend — Angular 20

| Tela | Descrição |
|---|---|
| Login | Autenticação do usuário |
| Cadastro | Criação de conta |
| Dashboard | Visão geral financeira consolidada |
| Lançamentos | Gestão de receitas e despesas |
| Centro de Custo | Gestão de categorias financeiras |

Guards de autenticação e interceptor HTTP para envio automático do token JWT em todas as requisições.

---

## 🚀 Como Executar

### Pré-requisitos
- Docker e Docker Compose instalados
- Java 17+
- Node.js 18+ (para o frontend)

### Subindo o projeto completo

```bash
# Clone a infraestrutura
git clone https://github.com/TLB-TECH/controle-financeiro-infra.git
cd controle-financeiro-infra

# Configure as variáveis de ambiente
cp .env.example .env
# Edite o .env com suas credenciais

# Sobe todos os serviços
docker compose up -d

# Acompanha os logs
docker compose logs -f
```

### Variáveis de ambiente necessárias (.env)

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

### Serviços disponíveis após o start

| Serviço | URL |
|---|---|
| Frontend Angular | http://localhost:4200 |
| API Gateway | http://localhost:8080 |
| BFF | http://localhost:8085/swagger-ui.html |
| ms-usuarios | http://localhost:8081/swagger-ui.html |
| ms-centro-custo | http://localhost:8082/swagger-ui.html |
| ms-lancamentos | http://localhost:8083/swagger-ui.html |
| ms-fluxo-caixa | http://localhost:8084/swagger-ui.html |
| ms-notificacao | http://localhost:8086/swagger-ui.html |
| Evolution API | http://localhost:8089 |

---

## 🗺️ Roadmap

- [x] Definição da arquitetura
- [x] Configuração dos bancos de dados
- [x] ms-usuarios — CRUD + JWT
- [x] ms-centro-custo — CRUD
- [x] ms-lancamentos — CRUD + parcelamentos + cartão de crédito
- [x] ms-fluxo-caixa — Relatórios + Resilience4j
- [x] bff-financeiro — Agregação de dados (dashboard)
- [x] API Gateway — Roteamento + Segurança JWT
- [x] ms-notificacao — E-mail + WhatsApp + scheduler
- [x] Docker Compose — Orquestração completa
- [x] Frontend Angular — Login, Cadastro, Dashboard, Lançamentos, Centro de Custo
- [ ] Testes unitários e de integração (meta: 80%+)
- [ ] CI/CD com GitHub Actions

---

## 🧪 Testes

```bash
# Roda os testes de um microsserviço
cd ms-lancamentos
./mvnw test

# Roda com relatório de cobertura
./mvnw test jacoco:report
```

Meta de cobertura: **80%+**

---

## 📂 Repositórios

| Repositório | Descrição |
|---|---|
| [controle-financeiro](https://github.com/TLB-TECH/controle-financeiro) | Monorepo principal |
| [ms-api-gateway](https://github.com/TLB-TECH/ms-api-gateway) | API Gateway |
| [bff-financeiro](https://github.com/TLB-TECH/bff-financeiro) | BFF |
| [ms-centro-custo](https://github.com/TLB-TECH/ms-centro-custo) | Centro de Custo |
| [ms-fluxo-caixa](https://github.com/TLB-TECH/ms-fluxo-caixa) | Fluxo de Caixa |
| [ms-notificacao](https://github.com/TLB-TECH/ms-notificacao) | Notificações |
| [controle-financeiro-infra](https://github.com/TLB-TECH/controle-financeiro-infra) | Infraestrutura Docker |
| [controle-financeiro-usuario-front](https://github.com/TLB-TECH/controle-financeiro-usuario-front) | Frontend Angular |

---

## 👨‍💻 Autor

**Tacio** — [@Taciolb](https://github.com/Taciolb) | [TLB-TECH](https://github.com/TLB-TECH)

---

## 📄 Licença

Este projeto está sob a licença MIT.
