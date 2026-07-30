import { describe, it, expect } from 'vitest'
import { decodeJwt, isJwtExpired } from './jwt'

function makeToken(payload: Record<string, unknown>): string {
  const base64url = (input: string) =>
    btoa(input).replace(/\+/g, '-').replace(/\//g, '_').replace(/=+$/, '')

  const header = base64url(JSON.stringify({ alg: 'HS512' }))
  const body = base64url(JSON.stringify(payload))
  return `${header}.${body}.fake-signature`
}

describe('decodeJwt', () => {
  it('decodes the payload of a well-formed token', () => {
    const token = makeToken({ sub: 'user@email.com', role: 'ADMIN' })

    expect(decodeJwt(token)).toEqual({ sub: 'user@email.com', role: 'ADMIN' })
  })

  it('returns null for a malformed token', () => {
    expect(decodeJwt('not-a-valid-jwt')).toBeNull()
  })

  it('returns null for an empty string', () => {
    expect(decodeJwt('')).toBeNull()
  })
})

describe('isJwtExpired', () => {
  it('returns false when exp is in the future', () => {
    const futureExp = Math.floor(Date.now() / 1000) + 3600
    const token = makeToken({ sub: 'a@b.com', exp: futureExp })

    expect(isJwtExpired(token)).toBe(false)
  })

  it('returns true when exp is in the past', () => {
    const pastExp = Math.floor(Date.now() / 1000) - 3600
    const token = makeToken({ sub: 'a@b.com', exp: pastExp })

    expect(isJwtExpired(token)).toBe(true)
  })

  it('returns false when there is no exp claim', () => {
    const token = makeToken({ sub: 'a@b.com' })

    expect(isJwtExpired(token)).toBe(false)
  })
})
