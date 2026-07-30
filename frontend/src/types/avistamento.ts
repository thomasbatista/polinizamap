export type StatusAvistamento = 'PENDENTE' | 'APROVADO' | 'REJEITADO'

export interface AvistamentoRequest {
  especieId: number
  regiaoId: number
  latitude: number
  longitude: number
  descricao: string
  dataHora: string
}

export interface AvistamentoResponse {
  id: number
  usuarioNome: string
  especieNomePopular: string
  regiaoNome: string
  regiaoCidade: string
  latitude: number
  longitude: number
  descricao: string | null
  dataHora: string
  status: StatusAvistamento
  notaValidacao: string | null
}

export interface ValidacaoRequest {
  status: StatusAvistamento
  notaValidacao?: string | null
}
