
export enum EstadoTransaccion {
    SOLICITAR = 'SOLICITAR',
    RESERVA = 'RESERVA',
    SENIADO = 'SENIADO',
    VENDIDO = 'VENDIDO',
    CANCELADO = 'CANCELADO',
}

export enum MetodoPago {
    EFECTIVO = 'EFECTIVO',
    TRANSFERENCIA = 'TRANSFERENCIA',
    PLAN_AHORRO = 'PLAN_AHORRO',
    CUOTA_BANCARIZADA = 'CUOTA_BANCARIZADA',
}

export const ETIQUETA_ESTADO: Record<EstadoTransaccion, string> = {
  [EstadoTransaccion.SOLICITAR]: 'Solicitada',
  [EstadoTransaccion.RESERVA]: 'Reserva',
  [EstadoTransaccion.SENIADO]: 'Señada',
  [EstadoTransaccion.VENDIDO]: 'Vendida',
  [EstadoTransaccion.CANCELADO]: 'Cancelada',
};

export const ETIQUETA_METODO_PAGO: Record<MetodoPago, string> = {
  [MetodoPago.EFECTIVO]: 'Efectivo',
  [MetodoPago.TRANSFERENCIA]: 'Transferencia',
  [MetodoPago.PLAN_AHORRO]: 'Plan de ahorro',
  [MetodoPago.CUOTA_BANCARIZADA]: 'Cuota bancarizada',
};

export const ETIQUETA_SOLICITUD = {
  sinAsignar: 'Sin asignar',
  asignada: 'Asignada',
  visita: 'Visita',
};

export const SIN_OBSERVACIONES = 'Sin observaciones';

export interface Transaccion {
    id: number;
    observaciones: string | null;
    precio_final: number | null;
    metodoPago: MetodoPago | null;
    estadoTransaccion: EstadoTransaccion;
    vehiculo_id: number;
    patente: string;
    cliente_id: number;
    cliente_email: string;
    vendedor_id: number | null;
    vendedor_email: string | null;
    fecha_visita: string | null;
}

export interface FiltrosTransaccion {
    estadoTransaccion?: EstadoTransaccion;
    fechaDesde?: string;
    fechaHasta?: string;
}

export interface CrearTransaccion {
    observaciones?: string;
    precio_final: number;
    metodoPago: MetodoPago;
    vehiculo_id: number;
    cliente_id: number;
    vendedor_id: number;
}

export interface SolicitarTransaccion {
    observaciones?: string;
    metodoPago?: MetodoPago;
    vehiculo_id: number;
}

export interface AsignarVisita {
    fechaVisita: string | null;
}

export interface ActualizarTransaccion extends CrearTransaccion {
    estadoTransaccion: EstadoTransaccion;
}

export interface BalanceTransacciones {
    precios_final_total: number;
    comisiones_total: number;
    costos_vehiculos_vendidos: number;
    ingreso_final: number;
    fecha_desde: string;
    fecha_hasta: string;
}