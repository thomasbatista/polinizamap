import { useEffect, useState } from 'react'
import * as especieApi from '../api/especieApi'
import type { EspecieResponse } from '../types/especie'

export function useEspecies() {
  const [especies, setEspecies] = useState<EspecieResponse[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    let ativo = true

    especieApi
      .listarEspecies()
      .then((data) => {
        if (ativo) setEspecies(data)
      })
      .finally(() => {
        if (ativo) setLoading(false)
      })

    return () => {
      ativo = false
    }
  }, [])

  return { especies, loading }
}
