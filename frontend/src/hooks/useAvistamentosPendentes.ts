import { useCallback, useEffect, useState } from 'react'
import * as avistamentoApi from '../api/avistamentoApi'
import type { AvistamentoResponse } from '../types/avistamento'

export function useAvistamentosPendentes() {
  const [avistamentos, setAvistamentos] = useState<AvistamentoResponse[]>([])
  const [loading, setLoading] = useState(true)

  const recarregar = useCallback(async () => {
    setLoading(true)
    try {
      const data = await avistamentoApi.listarAvistamentos('PENDENTE')
      setAvistamentos(data)
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    recarregar()
  }, [recarregar])

  return { avistamentos, loading, recarregar }
}
