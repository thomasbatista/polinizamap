import { apiClient } from './client'
import type { AvistamentoRequest, AvistamentoResponse } from '../types/avistamento'

export async function listarAvistamentos(): Promise<AvistamentoResponse[]> {
  const { data } = await apiClient.get<AvistamentoResponse[]>('/avistamentos')
  return data
}

export async function criarAvistamento(request: AvistamentoRequest): Promise<AvistamentoResponse> {
  const { data } = await apiClient.post<AvistamentoResponse>('/avistamentos', request)
  return data
}
