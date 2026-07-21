import { apiClient } from './client'
import type { EspecieResponse } from '../types/especie'

export async function listarEspecies(): Promise<EspecieResponse[]> {
  const { data } = await apiClient.get<EspecieResponse[]>('/especies')
  return data
}
