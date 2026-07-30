import { describe, it, expect, vi } from 'vitest'
import { render, screen } from '@testing-library/react'
import { MemoryRouter, Route, Routes } from 'react-router-dom'
import { PrivateRoute } from './PrivateRoute'
import { useAuth } from '../hooks/useAuth'

vi.mock('../hooks/useAuth')

const mockedUseAuth = vi.mocked(useAuth)

function renderWithRoute(allowedRoles?: Array<'CIDADAO' | 'PESQUISADOR' | 'ADMIN'>) {
  return render(
    <MemoryRouter initialEntries={['/protegida']}>
      <Routes>
        <Route path="/login" element={<div>Tela de login</div>} />
        <Route path="/mapa" element={<div>Tela de mapa</div>} />
        <Route element={<PrivateRoute allowedRoles={allowedRoles} />}>
          <Route path="/protegida" element={<div>Conteúdo protegido</div>} />
        </Route>
      </Routes>
    </MemoryRouter>,
  )
}

describe('PrivateRoute', () => {
  it('redireciona para /login quando não autenticado', () => {
    mockedUseAuth.mockReturnValue({
      isAuthenticated: false,
      userEmail: null,
      userRole: null,
      login: vi.fn(),
      register: vi.fn(),
      logout: vi.fn(),
    })

    renderWithRoute()

    expect(screen.getByText('Tela de login')).toBeInTheDocument()
  })

  it('renderiza o conteúdo quando autenticado e sem restrição de papel', () => {
    mockedUseAuth.mockReturnValue({
      isAuthenticated: true,
      userEmail: 'cidadao@email.com',
      userRole: 'CIDADAO',
      login: vi.fn(),
      register: vi.fn(),
      logout: vi.fn(),
    })

    renderWithRoute()

    expect(screen.getByText('Conteúdo protegido')).toBeInTheDocument()
  })

  it('redireciona para /mapa quando o papel do usuário não está na lista permitida', () => {
    mockedUseAuth.mockReturnValue({
      isAuthenticated: true,
      userEmail: 'cidadao@email.com',
      userRole: 'CIDADAO',
      login: vi.fn(),
      register: vi.fn(),
      logout: vi.fn(),
    })

    renderWithRoute(['PESQUISADOR', 'ADMIN'])

    expect(screen.getByText('Tela de mapa')).toBeInTheDocument()
  })

  it('renderiza o conteúdo quando o papel do usuário está na lista permitida', () => {
    mockedUseAuth.mockReturnValue({
      isAuthenticated: true,
      userEmail: 'admin@email.com',
      userRole: 'ADMIN',
      login: vi.fn(),
      register: vi.fn(),
      logout: vi.fn(),
    })

    renderWithRoute(['PESQUISADOR', 'ADMIN'])

    expect(screen.getByText('Conteúdo protegido')).toBeInTheDocument()
  })
})
