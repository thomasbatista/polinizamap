# PolinizaMap — Frontend

Frontend em React + Vite + TypeScript + Tailwind CSS para a API PolinizaMap.

## Requisitos

- Node.js 18+
- Backend do PolinizaMap rodando (por padrão em `http://localhost:8080`)

## Como rodar

```bash
npm install
npm run dev
```

A aplicação sobe em `http://localhost:5173`.

## Variável de ambiente

Crie um arquivo `.env` na raiz do `frontend/` (já existe um com o valor padrão) com:

```
VITE_API_URL=http://localhost:8080
```

Aponte para a URL onde a API Spring Boot está rodando.

## Estrutura

- `src/api/` — client HTTP (axios) e funções de chamada por domínio (`authApi`, `especieApi`, `regiaoApi`, `avistamentoApi`), além do tratamento centralizado de erros (`erroHandler.ts`)
- `src/types/` — interfaces TypeScript espelhando os DTOs do backend, um arquivo por domínio
- `src/context/` — `AuthContext`, estado global de autenticação (token JWT)
- `src/hooks/` — hooks de domínio com lógica de carregamento/erro (`useEspecies`, `useRegioes`, `useAvistamentos`, `useAuth`)
- `src/pages/` — telas da aplicação (login, registro, mapa, novo avistamento)
- `src/components/` — UI reutilizável (layout, rota privada, seletor de local no mapa, toasts, spinner)
- `src/lib/` — utilitários (decodificação de JWT, armazenamento de token, barramento de toasts, ícones do Leaflet)

## Autenticação

- Login (`/login`) e cadastro (`/register`) são públicos. Após autenticar, o token JWT é salvo no `localStorage` e enviado em todas as chamadas via header `Authorization: Bearer <token>`.
- Cadastros feitos pelo frontend usam sempre o papel `CIDADAO`.
- Como o token só carrega o email do usuário (sem nome), o header exibe o email do usuário logado.
- Rotas `/mapa` e `/avistamentos/novo` são protegidas: sem token válido, o usuário é redirecionado para `/login`. Uma resposta `401` da API também desloga o usuário automaticamente.

## Novo Avistamento

Como a região não carrega coordenadas próprias (só nome/cidade/estado), a latitude/longitude do avistamento é marcada clicando diretamente no mapa dentro do formulário.

## Não incluído nesta versão

- Tela de validação de avistamentos (pesquisador/admin)
- Testes automatizados
- Refresh token / login social
