import { apiClient } from './client'
import type { RegiaoResponse } from '../types/regiao'

export async function listarRegioes(): Promise<RegiaoResponse[]> {
  const { data } = await apiClient.get<RegiaoResponse[]>('/regioes')
  return data
}
