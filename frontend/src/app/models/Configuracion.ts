export interface Configuracion {
  porcentaje_senia: number;
  porcentaje_maximo_reserva: number;
}

/** Mismo rango que valida el backend. */
export const RANGO_PORCENTAJE_SENIA = { minimo: 1, maximo: 50 };
