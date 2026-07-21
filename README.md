# PolinizaMap

REST API and web frontend for collaborative monitoring of local pollinator fauna, where citizens register sightings of pollinating animals (bees, butterflies, hummingbirds). Researchers and admins can review and validate the data.

## Technologies

**Backend**
- Java 17 + Spring Boot 3.5.3
- Spring Security + JWT
- Spring Data JPA + PostgreSQL
- Flyway
- SpringDoc OpenAPI (Swagger UI)
- Docker

**Frontend**
- React 18 + TypeScript
- Vite
- Tailwind CSS 4
- React Router
- Axios
- React Leaflet (interactive map with OpenStreetMap)

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

# Run the application
./mvnw spring-boot:run
```

API available at `http://localhost:8080`. Swagger available at `http://localhost:8080/swagger-ui.html`.

### Frontend

```bash
cd frontend
npm install
npm run dev
```

App available at `http://localhost:5173`. Requires a `.env` file with `VITE_API_URL=http://localhost:8080` (already present with this default value).

## Authentication

The API uses JWT. After registering and logging in, send the token in the header:
Authorization: Bearer <token>

Users registered through the frontend are always created with the `CIDADAO` role.

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

## Frontend routes

| Route | Access | Description |
|--------|--------|-------------|
| `/login` | Public | Login |
| `/register` | Public | User registration |
| `/mapa` | Private | Map with the user's sightings |
| `/avistamentos/novo` | Private | New sighting form |

Sighting coordinates are set by clicking directly on a map in the new sighting form, since regions don't carry their own coordinates.
