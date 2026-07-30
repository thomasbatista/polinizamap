# PolinizaMap

![CI](https://github.com/thomasbatista/polinizamap/actions/workflows/ci.yml/badge.svg)

REST API and web frontend for collaborative monitoring of local pollinator fauna, where citizens register sightings of pollinating animals (bees, butterflies, hummingbirds). Researchers and admins can review and validate the data.

## Technologies

**Backend**
- Java 17 + Spring Boot 3.5.3
- Spring Security + JWT
- Spring Data JPA + PostgreSQL
- Flyway
- Spring Boot Actuator (health check)
- SpringDoc OpenAPI (Swagger UI)
- Docker

**Frontend**
- React 18 + TypeScript
- Vite
- Tailwind CSS 4
- React Router
- Axios
- React Leaflet (interactive map with OpenStreetMap)
- Vitest + Testing Library

## Project structure

```
backend/     Spring Boot REST API
frontend/    React web client
```

## How to run

### Backend

```bash
cd backend

# Start the database
docker compose up -d

# Run the application (requires JWT_KEY set, see below)
./mvnw spring-boot:run
```

Requires a `JWT_KEY` environment variable (a long random secret used to sign JWTs — the app refuses to start without it). API available at `http://localhost:8080`. Swagger available at `http://localhost:8080/swagger-ui.html`. Health check at `http://localhost:8080/actuator/health`.

Optional environment variables (all default to local dev values if unset): `DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD`, `CORS_ALLOWED_ORIGINS` (comma-separated list).

### Frontend

```bash
cd frontend
npm install
npm run dev
```

App available at `http://localhost:5173`. Requires a `.env` file with `VITE_API_URL=http://localhost:8080` (already present with this default value).

## Demo accounts

Seeded via a Flyway migration ([`V6__seed_dados_demo.sql`](backend/src/main/resources/db/migration/V6__seed_dados_demo.sql)) so a fresh database isn't empty on first run:

| Email | Password | Role |
|---|---|---|
| `visitante@polinizamap.com` | `polinizamap123` | CIDADAO |
| `pesquisador.demo@polinizamap.com` | `polinizamap123` | PESQUISADOR |

## Authentication

The API uses JWT. After registering and logging in, send the token in the header:
Authorization: Bearer <token>

Users registered through the frontend are always created with the `CIDADAO` role. The JWT carries the user's `role` as a claim, which the frontend uses to show/hide role-gated screens (e.g. sighting validation) without an extra request.

## Roles

| Role | Permissions |
|------|-------------|
| CIDADAO | Register and view own sightings |
| PESQUISADOR | View and validate all sightings |
| ADMIN | Full access + manage species and regions |

## Main API endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/auth/register` | Register user |
| POST | `/auth/login` | Login |
| GET/POST | `/especies` | List and create species |
| GET/POST | `/regioes` | List and create regions |
| GET/POST | `/avistamentos` | List and register sightings |
| PATCH | `/avistamentos/{id}/validar` | Validate sighting |
| GET | `/actuator/health` | Health check (public) |

## Frontend routes

| Route | Access | Description |
|--------|--------|-------------|
| `/login` | Public | Login |
| `/register` | Public | User registration |
| `/mapa` | Private | Map with sightings |
| `/avistamentos/novo` | Private | New sighting form |
| `/validacao` | Private (PESQUISADOR/ADMIN) | Review and approve/reject pending sightings |

Sighting coordinates are set by clicking directly on a map in the new sighting form, since regions don't carry their own coordinates.

## Testing

```bash
# Backend
cd backend && ./mvnw test

# Frontend
cd frontend && npm run test
```

CI (GitHub Actions) runs both suites plus a production build on every push/PR to `main` — see [`.github/workflows/ci.yml`](.github/workflows/ci.yml).

## Known limitations

- **Refresh tokens / social login**: out of scope by design. JWTs expire after 24h, so users just log in again — acceptable for this project's scope.
- **`react-router-dom` moderate CVEs**: fixable only by a v6→v7 major upgrade. Reviewed both advisories — the open-redirect one needs attacker-controlled input feeding a `<Link>`/`useNavigate` target, which doesn't happen here (all routes are static); the other only affects SSR hydration, and this app is a client-only SPA with no SSR. Deferred as a standalone follow-up rather than rushed before deploy.
