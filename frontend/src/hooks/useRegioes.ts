import { useEffect, useState } from 'react'
import * as regiaoApi from '../api/regiaoApi'
import type { RegiaoResponse } from '../types/regiao'

export function useRegioes() {
  const [regioes, setRegioes] = useState<RegiaoResponse[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    let ativo = true

    regiaoApi
      .listarRegioes()
      .then((data) => {
        if (ativo) setRegioes(data)
      })
      .finally(() => {
        if (ativo) setLoading(false)
      })

    return () => {
      ativo = false
    }
  }, [])

  return { regioes, loading }
}
