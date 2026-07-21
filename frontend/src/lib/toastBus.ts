export type ToastType = 'error' | 'success' | 'info'

export interface Toast {
  id: number
  type: ToastType
  message: string
}

type Listener = (toast: Toast) => void

let nextId = 1
const listeners = new Set<Listener>()

export function emitToast(message: string, type: ToastType = 'error'): void {
  const toast: Toast = { id: nextId++, type, message }
  listeners.forEach((listener) => listener(toast))
}

export function subscribeToast(listener: Listener): () => void {
  listeners.add(listener)
  return () => listeners.delete(listener)
}
