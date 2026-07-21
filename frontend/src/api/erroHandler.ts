import axios from 'axios'
import type { ErrorResponse } from '../types/erro'

export class ApiError extends Error {
  status: number
  errors: string[] | null

  constructor(status: number, message: string, errors: string[] | null) {
    super(message)
    this.name = 'ApiError'
    this.status = status
    this.errors = errors
  }
}

export function parseApiError(error: unknown): ApiError {
  if (axios.isAxiosError(error)) {
    const data = error.response?.data as ErrorResponse | undefined

    if (data?.message) {
      return new ApiError(data.status, data.message, data.errors)
    }

    if (error.response?.status === 401) {
      return new ApiError(401, 'Sessão expirada. Faça login novamente.', null)
    }

    if (!error.response) {
      return new ApiError(0, 'Não foi possível conectar à API. Verifique sua conexão.', null)
    }

    return new ApiError(error.response.status, 'Erro inesperado ao comunicar com a API.', null)
  }

  return new ApiError(500, 'Erro inesperado.', null)
}
