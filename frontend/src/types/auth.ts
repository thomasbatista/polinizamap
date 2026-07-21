export type Role = 'CIDADAO' | 'PESQUISADOR' | 'ADMIN'

export interface LoginRequest {
  email: string
  senha: string
}

export interface RegisterRequest {
  nome: string
  email: string
  senha: string
  role: Role
}

export interface AuthResponse {
  token: string
}
