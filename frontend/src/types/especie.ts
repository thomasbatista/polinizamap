export type StatusConservacao =
  | 'POUCO_PREOCUPANTE'
  | 'QUASE_AMEACADA'
  | 'VULNERAVEL'
  | 'EM_PERIGO'
  | 'CRITICAMENTE_EM_PERIGO'

export interface EspecieResponse {
  id: number
  nomePopular: string
  nomeCientifico: string
  statusConservacao: StatusConservacao
}
