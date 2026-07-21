const TOKEN_KEY = 'polinizamap_token'
export const AUTH_LOGOUT_EVENT = 'polinizamap:auth-logout'

export function getToken(): string | null {
  return localStorage.getItem(TOKEN_KEY)
}

export function setToken(token: string): void {
  localStorage.setItem(TOKEN_KEY, token)
}

export function clearToken(): void {
  localStorage.removeItem(TOKEN_KEY)
}
