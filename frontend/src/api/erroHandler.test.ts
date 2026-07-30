import { describe, it, expect } from 'vitest'
import { parseApiError, ApiError } from './erroHandler'

function fakeAxiosError(overrides: { response?: unknown } = {}) {
  return {
    isAxiosError: true,
    response: overrides.response,
    message: 'request failed',
  }
}

describe('parseApiError', () => {
  it('uses the backend ErrorResponse message and errors when present', () => {
    const error = fakeAxiosError({
      response: {
        status: 400,
        data: {
          status: 400,
          message: 'Erro de validação',
          timestamp: '2026-01-01T00:00:00',
          errors: ['email: Email inválido'],
        },
      },
    })

    const result = parseApiError(error)

    expect(result).toBeInstanceOf(ApiError)
    expect(result.status).toBe(400)
    expect(result.message).toBe('Erro de validação')
    expect(result.errors).toEqual(['email: Email inválido'])
  })

  it('falls back to a friendly message on 401 with no body', () => {
    const error = fakeAxiosError({ response: { status: 401, data: undefined } })

    const result = parseApiError(error)

    expect(result.status).toBe(401)
    expect(result.message).toMatch(/sessão expirada/i)
  })

  it('reports a connection error when there is no response at all', () => {
    const error = fakeAxiosError({ response: undefined })

    const result = parseApiError(error)

    expect(result.status).toBe(0)
    expect(result.message).toMatch(/não foi possível conectar/i)
  })

  it('falls back to a generic message for other status codes with no body', () => {
    const error = fakeAxiosError({ response: { status: 500, data: undefined } })

    const result = parseApiError(error)

    expect(result.status).toBe(500)
    expect(result.message).toMatch(/erro inesperado/i)
  })

  it('returns a generic ApiError for non-axios errors', () => {
    const result = parseApiError(new Error('boom'))

    expect(result).toBeInstanceOf(ApiError)
    expect(result.status).toBe(500)
  })
})
