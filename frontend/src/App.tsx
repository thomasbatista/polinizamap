import { Navigate, Route, Routes } from 'react-router-dom'
import { AuthProvider } from './context/AuthContext'
import { PrivateRoute } from './components/PrivateRoute'
import { Layout } from './components/Layout'
import { ToastContainer } from './components/ToastContainer'
import { LoginPage } from './pages/LoginPage'
import { RegisterPage } from './pages/RegisterPage'
import { MapaPage } from './pages/MapaPage'
import { NovoAvistamentoPage } from './pages/NovoAvistamentoPage'
import { ValidacaoPage } from './pages/ValidacaoPage'

export function App() {
  return (
    <AuthProvider>
      <ToastContainer />
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />

        <Route element={<PrivateRoute />}>
          <Route element={<Layout />}>
            <Route path="/mapa" element={<MapaPage />} />
            <Route path="/avistamentos/novo" element={<NovoAvistamentoPage />} />
          </Route>
        </Route>

        <Route element={<PrivateRoute allowedRoles={['PESQUISADOR', 'ADMIN']} />}>
          <Route element={<Layout />}>
            <Route path="/validacao" element={<ValidacaoPage />} />
          </Route>
        </Route>

        <Route path="/" element={<Navigate to="/mapa" replace />} />
        <Route path="*" element={<Navigate to="/mapa" replace />} />
      </Routes>
    </AuthProvider>
  )
}
