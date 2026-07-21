import { useEffect, useState } from 'react'
import { subscribeToast, type Toast } from '../lib/toastBus'

const TOAST_DURATION_MS = 5000

const STYLES_BY_TYPE: Record<Toast['type'], string> = {
  error: 'bg-red-600',
  success: 'bg-emerald-600',
  info: 'bg-slate-700',
}

export function ToastContainer() {
  const [toasts, setToasts] = useState<Toast[]>([])

  useEffect(() => {
    return subscribeToast((toast) => {
      setToasts((current) => [...current, toast])
      setTimeout(() => {
        setToasts((current) => current.filter((t) => t.id !== toast.id))
      }, TOAST_DURATION_MS)
    })
  }, [])

  if (toasts.length === 0) return null

  return (
    <div className="fixed bottom-4 right-4 z-50 flex flex-col gap-2">
      {toasts.map((toast) => (
        <div
          key={toast.id}
          className={`rounded-lg px-4 py-3 text-sm text-white shadow-lg ${STYLES_BY_TYPE[toast.type]}`}
        >
          {toast.message}
        </div>
      ))}
    </div>
  )
}
