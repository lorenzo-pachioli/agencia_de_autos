import { HttpErrorResponse } from '@angular/common/http';
import { signal } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { Observable } from 'rxjs';
import type { ApiError } from '../models/ApiError';

export const ERROR_OPERACION = 'No se pudo completar la operación. Revisá los datos e intentá nuevamente.';
export const ERROR_CAMPOS = 'Revisá los campos marcados.';
const ERROR_CONEXION = 'No se pudo conectar con el servidor.';

/** Convierte un error HTTP en un texto para mostrar. Los errores 500 no traen un detalle útil. */
export function mensajeDeError(respuesta: HttpErrorResponse, porDefecto = ERROR_OPERACION): string {
  if (respuesta.status === 0) return ERROR_CONEXION;
  if (respuesta.status >= 500) return porDefecto;

  const detalle = respuesta.error as Partial<ApiError> | null;
  const validaciones = Object.values(detalle?.validationErrors ?? {});
  if (validaciones.length) return validaciones.join(' ');
  return detalle?.message || porDefecto;
}

/**
 * Envío desde un modal. Mientras dura no se puede cerrar; si falla muestra el motivo y el modal
 * queda abierto con lo cargado; si sale bien lo cierra devolviendo `resultado`.
 */
export class EnvioDeModal<R> {
  readonly enviando = signal(false);
  readonly error = signal('');

  constructor(private readonly dialogRef: MatDialogRef<unknown, R>) {}

  enviar(solicitud: Observable<unknown>, resultado: R, errorPorDefecto?: string): void {
    if (this.enviando()) return;
    this.marcarEnviando(true);
    this.error.set('');

    solicitud.subscribe({
      next: () => this.dialogRef.close(resultado),
      error: (respuesta: HttpErrorResponse) => {
        this.marcarEnviando(false);
        this.error.set(mensajeDeError(respuesta, errorPorDefecto));
      },
    });
  }

  private marcarEnviando(enviando: boolean): void {
    this.enviando.set(enviando);
    this.dialogRef.disableClose = enviando;
  }
}
