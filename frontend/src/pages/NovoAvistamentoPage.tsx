import { useState, type FormEvent } from 'react'
import { useNavigate } from 'react-router-dom'
import { useEspecies } from '../hooks/useEspecies'
import { useRegioes } from '../hooks/useRegioes'
import { criarAvistamento } from '../api/avistamentoApi'
import { ApiError } from '../api/erroHandler'
import { FormError } from '../components/FormError'
import { Spinner } from '../components/Spinner'
import { LocationPicker } from '../components/LocationPicker'
import { emitToast } from '../lib/toastBus'

function agoraParaInputDatetime(): string {
  const agora = new Date(Date.now() - new Date().getTimezoneOffset() * 60000)
  return agora.toISOString().slice(0, 16)
}

export function NovoAvistamentoPage() {
  const { especies, loading: carregandoEspecies } = useEspecies()
  const { regioes, loading: carregandoRegioes } = useRegioes()
  const navigate = useNavigate()

  const [especieId, setEspecieId] = useState('')
  const [regiaoId, setRegiaoId] = useState('')
  const [dataHora, setDataHora] = useState(agoraParaInputDatetime())
  const [descricao, setDescricao] = useState('')
  const [latitude, setLatitude] = useState<number | null>(null)
  const [longitude, setLongitude] = useState<number | null>(null)
  const [error, setError] = useState<ApiError | null>(null)
  const [enviando, setEnviando] = useState(false)

  const carregandoDropdowns = carregandoEspecies || carregandoRegioes

  async function handleSubmit(e: FormEvent) {
    e.preventDefault()
    setError(null)

    if (latitude === null || longitude === null) {
      setError(new ApiError(400, 'Clique no mapa para marcar o local do avistamento.', null))
      return
    }

    setEnviando(true)
    try {
      await criarAvistamento({
        especieId: Number(especieId),
        regiaoId: Number(regiaoId),
        latitude,
        longitude,
        descricao,
        dataHora: dataHora.length === 16 ? `${dataHora}:00` : dataHora,
      })
      emitToast('Avistamento registrado com sucesso!', 'success')
      navigate('/mapa')
    } catch (err) {
      setError(err as ApiError)
    } finally {
      setEnviando(false)
    }
  }

  if (carregandoDropdowns) {
    return <Spinner label="Carregando formulário..." />
  }

  return (
    <div className="mx-auto max-w-2xl px-4 py-8">
      <h1 className="mb-6 text-2xl font-semibold text-slate-800">Novo Avistamento</h1>

      <form onSubmit={handleSubmit} className="flex flex-col gap-4">
        <FormError error={error} />

        <div className="flex flex-col gap-1">
          <label htmlFor="especie" className="text-sm font-medium text-slate-700">
            Espécie
          </label>
          <select
            id="especie"
            required
            value={especieId}
            onChange={(e) => setEspecieId(e.target.value)}
            className="rounded-md border border-slate-300 px-3 py-2 text-sm focus:border-emerald-500 focus:outline-none focus:ring-1 focus:ring-emerald-500"
          >
            <option value="" disabled>
              Selecione uma espécie
            </option>
            {especies.map((especie) => (
              <option key={especie.id} value={especie.id}>
                {especie.nomePopular}
              </option>
            ))}
          </select>
        </div>

        <div className="flex flex-col gap-1">
          <label htmlFor="regiao" className="text-sm font-medium text-slate-700">
            Região
          </label>
          <select
            id="regiao"
            required
            value={regiaoId}
            onChange={(e) => setRegiaoId(e.target.value)}
            className="rounded-md border border-slate-300 px-3 py-2 text-sm focus:border-emerald-500 focus:outline-none focus:ring-1 focus:ring-emerald-500"
          >
            <option value="" disabled>
              Selecione uma região
            </option>
            {regioes.map((regiao) => (
              <option key={regiao.id} value={regiao.id}>
                {regiao.nome} — {regiao.cidade}/{regiao.estado}
              </option>
            ))}
          </select>
        </div>

        <div className="flex flex-col gap-1">
          <label htmlFor="dataHora" className="text-sm font-medium text-slate-700">
            Data e hora
          </label>
          <input
            id="dataHora"
            type="datetime-local"
            required
            max={agoraParaInputDatetime()}
            value={dataHora}
            onChange={(e) => setDataHora(e.target.value)}
            className="rounded-md border border-slate-300 px-3 py-2 text-sm focus:border-emerald-500 focus:outline-none focus:ring-1 focus:ring-emerald-500"
          />
        </div>

        <div className="flex flex-col gap-1">
          <label htmlFor="descricao" className="text-sm font-medium text-slate-700">
            Observações
          </label>
          <textarea
            id="descricao"
            rows={3}
            value={descricao}
            onChange={(e) => setDescricao(e.target.value)}
            className="rounded-md border border-slate-300 px-3 py-2 text-sm focus:border-emerald-500 focus:outline-none focus:ring-1 focus:ring-emerald-500"
          />
        </div>

        <div className="flex flex-col gap-1">
          <span className="text-sm font-medium text-slate-700">
            Local do avistamento {latitude !== null && longitude !== null && (
              <span className="font-normal text-slate-500">
                ({latitude.toFixed(5)}, {longitude.toFixed(5)})
              </span>
            )}
          </span>
          <p className="mb-1 text-xs text-slate-500">Clique no mapa para marcar onde o avistamento ocorreu.</p>
          <LocationPicker
            latitude={latitude}
            longitude={longitude}
            onSelecionar={(lat, lng) => {
              setLatitude(lat)
              setLongitude(lng)
            }}
          />
        </div>

        <button
          type="submit"
          disabled={enviando}
          className="mt-2 rounded-md bg-emerald-600 px-4 py-2 text-sm font-medium text-white transition hover:bg-emerald-700 disabled:opacity-60"
        >
          {enviando ? 'Registrando...' : 'Registrar avistamento'}
        </button>
      </form>
    </div>
  )
}
