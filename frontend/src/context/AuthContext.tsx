import { createContext, useEffect, useState, type ReactNode } from 'react'
import * as authApi from '../api/authApi'
import type { LoginRequest, RegisterRequest } from '../types/auth'
import { decodeJwt, isJwtExpired } from '../lib/jwt'
import { getToken, setToken, clearToken, AUTH_LOGOUT_EVENT } from '../lib/tokenStorage'

interface AuthContextValue {
  isAuthenticated: boolean
  userEmail: string | null
  login: (request: LoginRequest) => Promise<void>
  register: (request: RegisterRequest) => Promise<void>
  logout: () => void
}

export const AuthContext = createContext<AuthContextValue | undefined>(undefined)

function extractEmail(token: string): string | null {
  const payload = decodeJwt(token)
  return typeof payload?.sub === 'string' ? payload.sub : null
}

function readValidToken(): string | null {
  const token = getToken()
  if (!token || isJwtExpired(token)) {
    clearToken()
    return null
  }
  return token
}

export function AuthProvider({ children }: { children: ReactNode }) {
  const [userEmail, setUserEmail] = useState<string | null>(() => {
    const token = readValidToken()
    return token ? extractEmail(token) : null
  })

  useEffect(() => {
    const handleLogout = () => setUserEmail(null)
    window.addEventListener(AUTH_LOGOUT_EVENT, handleLogout)
    return () => window.removeEventListener(AUTH_LOGOUT_EVENT, handleLogout)
  }, [])

  async function login(request: LoginRequest) {
    const { token } = await authApi.login(request)
    setToken(token)
    setUserEmail(extractEmail(token))
  }

  async function register(request: RegisterRequest) {
    const { token } = await authApi.register(request)
    setToken(token)
    setUserEmail(extractEmail(token))
  }

  function logout() {
    clearToken()
    setUserEmail(null)
  }

  return (
    <AuthContext.Provider
      value={{ isAuthenticated: userEmail !== null, userEmail, login, register, logout }}
    >
      {children}
    </AuthContext.Provider>
  )
}
