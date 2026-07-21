# PolinizaMap

REST API for collaborative monitoring of local pollinator fauna, where citizens register sightings of pollinating animals (bees, butterflies, hummingbirds). Researchers and admins can review and validate the data.

## Technologies

- Java 17 + Spring Boot 3.5.3
- Spring Security + JWT
- Spring Data JPA + PostgreSQL
- Flyway
- SpringDoc OpenAPI (Swagger UI)
- Docker

## How to run

```bash
# Start the database
docker compose up -d

# Run the application
./mvnw spring-boot:run
```

Swagger available at: `http://localhost:8080/swagger-ui.html`

## Authentication

The API uses JWT. After registering and logging in, send the token in the header:
Authorization: Bearer <token>

## Roles

| Role | Permissions |
|------|-------------|
| CIDADAO | Register and view own sightings |
| PESQUISADOR | View and validate all sightings |
| ADMIN | Full access + manage species and regions |

## Main endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/auth/register` | Register user |
| POST | `/auth/login` | Login |
| GET/POST | `/especies` | List and create species |
| GET/POST | `/regioes` | List and create regions |
| GET/POST | `/avistamentos` | List and register sightings |
| PATCH | `/avistamentos/{id}/validar` | Validate sighting |
