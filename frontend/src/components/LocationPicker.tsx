import { MapContainer, Marker, TileLayer, useMapEvents } from 'react-leaflet'
import '../lib/leafletIcons'

const CENTRO_PADRAO: [number, number] = [-14.235, -51.9253]

interface LocationPickerProps {
  latitude: number | null
  longitude: number | null
  onSelecionar: (latitude: number, longitude: number) => void
}

function ClickHandler({ onSelecionar }: Pick<LocationPickerProps, 'onSelecionar'>) {
  useMapEvents({
    click(e) {
      onSelecionar(e.latlng.lat, e.latlng.lng)
    },
  })
  return null
}

export function LocationPicker({ latitude, longitude, onSelecionar }: LocationPickerProps) {
  const posicao: [number, number] | null = latitude !== null && longitude !== null ? [latitude, longitude] : null

  return (
    <div className="overflow-hidden rounded-lg border border-slate-300">
      <MapContainer
        center={posicao ?? CENTRO_PADRAO}
        zoom={posicao ? 13 : 4}
        style={{ height: '320px', width: '100%' }}
      >
        <TileLayer
          attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        />
        <ClickHandler onSelecionar={onSelecionar} />
        {posicao && <Marker position={posicao} />}
      </MapContainer>
    </div>
  )
}
