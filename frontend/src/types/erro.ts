export interface ErrorResponse {
  status: number
  message: string
  timestamp: string
  errors: string[] | null
}
