import { MapContainer, Marker, Popup, TileLayer } from 'react-leaflet'
import '../lib/leafletIcons'
import { useAvistamentos } from '../hooks/useAvistamentos'
import { Spinner } from '../components/Spinner'
import type { StatusAvistamento } from '../types/avistamento'

const CENTRO_PADRAO: [number, number] = [-14.235, -51.9253]

const LABEL_STATUS: Record<StatusAvistamento, string> = {
  PENDENTE: 'Pendente',
  APROVADO: 'Aprovado',
  REJEITADO: 'Rejeitado',
}

const COR_STATUS: Record<StatusAvistamento, string> = {
  PENDENTE: 'text-amber-600',
  APROVADO: 'text-emerald-600',
  REJEITADO: 'text-red-600',
}

function formatarData(dataHora: string): string {
  return new Date(dataHora).toLocaleString('pt-BR')
}

export function MapaPage() {
  const { avistamentos, loading } = useAvistamentos()

  if (loading) {
    return <Spinner label="Carregando avistamentos..." />
  }

  const centro: [number, number] =
    avistamentos.length > 0 ? [avistamentos[0].latitude, avistamentos[0].longitude] : CENTRO_PADRAO

  return (
    <div className="h-[calc(100vh-64px)] w-full">
      <MapContainer center={centro} zoom={avistamentos.length > 0 ? 6 : 4} style={{ height: '100%', width: '100%' }}>
        <TileLayer
          attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        />
        {avistamentos.map((avistamento) => (
          <Marker key={avistamento.id} position={[avistamento.latitude, avistamento.longitude]}>
            <Popup>
              <div className="flex flex-col gap-1 text-sm">
                <span className="font-semibold">{avistamento.especieNomePopular}</span>
                <span>{avistamento.regiaoNome} — {avistamento.regiaoCidade}</span>
                <span>{formatarData(avistamento.dataHora)}</span>
                <span className={`font-medium ${COR_STATUS[avistamento.status]}`}>
                  {LABEL_STATUS[avistamento.status]}
                </span>
              </div>
            </Popup>
          </Marker>
        ))}
      </MapContainer>
    </div>
  )
}
