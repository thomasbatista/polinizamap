import { useState } from 'react'
import { useAvistamentosPendentes } from '../hooks/useAvistamentosPendentes'
import { validarAvistamento } from '../api/avistamentoApi'
import { Spinner } from '../components/Spinner'
import { emitToast } from '../lib/toastBus'
import type { AvistamentoResponse, StatusAvistamento } from '../types/avistamento'

function formatarData(dataHora: string): string {
  return new Date(dataHora).toLocaleString('pt-BR')
}

function AvistamentoCard({
  avistamento,
  onValidado,
}: {
  avistamento: AvistamentoResponse
  onValidado: () => void
}) {
  const [notaValidacao, setNotaValidacao] = useState('')
  const [enviando, setEnviando] = useState(false)

  async function validar(status: StatusAvistamento) {
    setEnviando(true)
    try {
      await validarAvistamento(avistamento.id, {
        status,
        notaValidacao: notaValidacao || undefined,
      })
      emitToast(
        status === 'APROVADO' ? 'Avistamento aprovado.' : 'Avistamento rejeitado.',
        'success',
      )
      onValidado()
    } catch {
      // erro já é exibido via toast pelo interceptor central do client HTTP
    } finally {
      setEnviando(false)
    }
  }

  return (
    <div className="flex flex-col gap-3 rounded-lg border border-slate-200 bg-white p-4 shadow-sm">
      <div className="flex flex-col gap-1">
        <span className="font-semibold text-slate-800">{avistamento.especieNomePopular}</span>
        <span className="text-sm text-slate-500">
          {avistamento.regiaoNome} — {avistamento.regiaoCidade}
        </span>
        <span className="text-sm text-slate-500">
          Avistado por {avistamento.usuarioNome} em {formatarData(avistamento.dataHora)}
        </span>
        {avistamento.descricao && (
          <p className="mt-1 text-sm text-slate-700">{avistamento.descricao}</p>
        )}
      </div>

      <div className="flex flex-col gap-1">
        <label htmlFor={`nota-${avistamento.id}`} className="text-sm font-medium text-slate-700">
          Nota de validação (opcional)
        </label>
        <textarea
          id={`nota-${avistamento.id}`}
          rows={2}
          value={notaValidacao}
          onChange={(e) => setNotaValidacao(e.target.value)}
          className="rounded-md border border-slate-300 px-3 py-2 text-sm focus:border-emerald-500 focus:outline-none focus:ring-1 focus:ring-emerald-500"
        />
      </div>

      <div className="flex gap-2">
        <button
          type="button"
          disabled={enviando}
          onClick={() => validar('APROVADO')}
          className="rounded-md bg-emerald-600 px-4 py-2 text-sm font-medium text-white transition hover:bg-emerald-700 disabled:opacity-60"
        >
          Aprovar
        </button>
        <button
          type="button"
          disabled={enviando}
          onClick={() => validar('REJEITADO')}
          className="rounded-md bg-red-600 px-4 py-2 text-sm font-medium text-white transition hover:bg-red-700 disabled:opacity-60"
        >
          Rejeitar
        </button>
      </div>
    </div>
  )
}

export function ValidacaoPage() {
  const { avistamentos, loading, recarregar } = useAvistamentosPendentes()

  if (loading) {
    return <Spinner label="Carregando avistamentos pendentes..." />
  }

  return (
    <div className="mx-auto max-w-2xl px-4 py-8">
      <h1 className="mb-6 text-2xl font-semibold text-slate-800">Validação de Avistamentos</h1>

      {avistamentos.length === 0 ? (
        <p className="text-sm text-slate-500">Nenhum avistamento pendente.</p>
      ) : (
        <div className="flex flex-col gap-4">
          {avistamentos.map((avistamento) => (
            <AvistamentoCard key={avistamento.id} avistamento={avistamento} onValidado={recarregar} />
          ))}
        </div>
      )}
    </div>
  )
}
