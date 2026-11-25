
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
    monto_entregado: number | null;
    precio_venta: number;
    metodoPago: MetodoPago | null;
    estadoTransaccion: EstadoTransaccion;
    vehiculo_id: number;
    modelo: string;
    patente: string;
    cliente_id: number;
    cliente_email: string;
    vendedor_id: number | null;
    vendedor_nombre: string;
    vendedor_apellido: string;
    vendedor_email: string | null;
    fecha_visita: string | null;
}

export interface FiltrosTransaccion {
    estadoTransaccion?: EstadoTransaccion;
    fechaDesde?: string;
    fechaHasta?: string;
}

export type TipoOperacion = EstadoTransaccion.RESERVA | EstadoTransaccion.SENIADO | EstadoTransaccion.VENDIDO;

export interface DatosOperacion {
    estadoTransaccion: TipoOperacion;
    /** Solo se informa en una reserva; en la seña y la venta lo calcula el backend. */
    monto_entregado: number | null;
}

export interface CrearTransaccion extends DatosOperacion {
    observaciones?: string;
    metodoPago: MetodoPago;
    vehiculo_id: number;
    /** Cliente ya cargado. Si no existe se envía cliente_email. */
    cliente_id?: number;
    cliente_email?: string;
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

export type ActualizarTransaccion = CrearTransaccion & { cliente_id: number };

export interface BalanceTransacciones {
    precios_final_total: number;
    comisiones_total: number;
    costos_vehiculos_vendidos: number;
    ingreso_final: number;
    fecha_desde: string;
    fecha_hasta: string;
}

export interface ComisionVendedor {
    id: number;
    comision_total: number;
    vendedor_id: number;
    nombre_completo: string;
    email: string;
}

export interface AuditoriaTransaccion {
    id: number;
    transaccion_id: number;
    vendedor_id: number | null;
    created_at: string;
    precioFinalAnterior: number | null;
    precioFinalNuevo: number | null;
    metodoPagoAnterior: MetodoPago | null;
    metodoPagoNuevo: MetodoPago | null;
    estadoAnterior: EstadoTransaccion | null;
    estadoNuevo: EstadoTransaccion | null;
}

export interface FiltrosAuditoria {
    transaccionId?: number;
    vendedorId?: number;
    fechaDesde?: string;
    fechaHasta?: string;
    precioFinalAnterior?: number;
    precioFinalNuevo?: number;
    metodoPagoAnterior?: MetodoPago;
    metodoPagoNuevo?: MetodoPago;
    estadoAnterior?: EstadoTransaccion;
    estadoNuevo?: EstadoTransaccion;
}