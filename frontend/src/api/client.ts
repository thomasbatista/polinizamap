import axios from 'axios'
import { getToken, clearToken, AUTH_LOGOUT_EVENT } from '../lib/tokenStorage'
import { emitToast } from '../lib/toastBus'
import { parseApiError } from './erroHandler'

export const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
})

apiClient.interceptors.request.use((config) => {
  const token = getToken()
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    const apiError = parseApiError(error)

    if (apiError.status === 401) {
      clearToken()
      window.dispatchEvent(new Event(AUTH_LOGOUT_EVENT))
    }

    emitToast(apiError.message, 'error')
    return Promise.reject(apiError)
  },
)
