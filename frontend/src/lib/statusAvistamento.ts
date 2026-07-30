import type { StatusAvistamento } from '../types/avistamento'

export const LABEL_STATUS: Record<StatusAvistamento, string> = {
  PENDENTE: 'Pendente',
  APROVADO: 'Aprovado',
  REJEITADO: 'Rejeitado',
}

export const COR_STATUS: Record<StatusAvistamento, string> = {
  PENDENTE: 'text-amber-600',
  APROVADO: 'text-emerald-600',
  REJEITADO: 'text-red-600',
}
