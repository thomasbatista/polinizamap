import { ApiError } from '../api/erroHandler'

export function FormError({ error }: { error: ApiError | null }) {
  if (!error) return null

  return (
    <div className="rounded-md border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
      <p className="font-medium">{error.message}</p>
      {error.errors && error.errors.length > 0 && (
        <ul className="mt-1 list-inside list-disc">
          {error.errors.map((mensagem) => (
            <li key={mensagem}>{mensagem}</li>
          ))}
        </ul>
      )}
    </div>
  )
}
