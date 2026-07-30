import { apiClient } from './client'
import type {
  AvistamentoRequest,
  AvistamentoResponse,
  StatusAvistamento,
  ValidacaoRequest,
} from '../types/avistamento'

export async function listarAvistamentos(status?: StatusAvistamento): Promise<AvistamentoResponse[]> {
  const { data } = await apiClient.get<AvistamentoResponse[]>('/avistamentos', {
    params: status ? { status } : undefined,
  })
  return data
}

export async function criarAvistamento(request: AvistamentoRequest): Promise<AvistamentoResponse> {
  const { data } = await apiClient.post<AvistamentoResponse>('/avistamentos', request)
  return data
}

export async function validarAvistamento(id: number, request: ValidacaoRequest): Promise<AvistamentoResponse> {
  const { data } = await apiClient.patch<AvistamentoResponse>(`/avistamentos/${id}/validar`, request)
  return data
}
