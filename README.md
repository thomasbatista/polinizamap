# 🐝 PolinizaMap

API REST de monitoramento colaborativo de fauna local, onde cidadãos registam avistamentos de animais polinizadores (abelhas, borboletas, beija-flores). Pesquisadores e admins podem consultar e validar os dados.

## Tecnologias

- Java 17 + Spring Boot 3.5.3
- Spring Security + JWT
- Spring Data JPA + PostgreSQL
- Flyway
- SpringDoc OpenAPI (Swagger UI)
- Docker

## Como rodar

```bash
# Sobe o banco
docker compose up -d

# Roda a aplicação
./mvnw spring-boot:run
```

Swagger disponível em: `http://localhost:8080/swagger-ui.html`

## Autenticação

A API usa JWT. Após registar e fazer login, enviar o token no header:

```
Authorization: Bearer <token>
```

## Roles

| Role | Permissões |
|---|---|
| `CIDADAO` | Registar e ver próprios avistamentos |
| `PESQUISADOR` | Ver e validar todos os avistamentos |
| `ADMIN` | Acesso total + gerir espécies e regiões |

## Endpoints principais

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/auth/register` | Cadastrar usuário |
| `POST` | `/auth/login` | Login |
| `GET/POST` | `/especies` | Listar e criar espécies |
| `GET/POST` | `/regioes` | Listar e criar regiões |
| `GET/POST` | `/avistamentos` | Listar e registar avistamentos |
| `PATCH` | `/avistamentos/{id}/validar` | Validar avistamento |
