import { Configuracion } from './Configuracion';
import { DatosOperacion, EstadoTransaccion, TipoOperacion } from './Transacciones';

export const TIPOS_OPERACION: TipoOperacion[] = [
  EstadoTransaccion.RESERVA,
  EstadoTransaccion.SENIADO,
  EstadoTransaccion.VENDIDO,
];

export const ETIQUETA_TIPO_OPERACION: Record<TipoOperacion, string> = {
  [EstadoTransaccion.RESERVA]: 'Reserva',
  [EstadoTransaccion.SENIADO]: 'Seña',
  [EstadoTransaccion.VENDIDO]: 'Venta',
};

export function formatearMonto(monto: number): string {
  return new Intl.NumberFormat('es-AR', { maximumFractionDigits: 2 }).format(monto);
}

export function maximoReserva(precio: number, configuracion: Configuracion): number {
  return Math.floor(precio * configuracion.porcentaje_maximo_reserva) / 100;
}

/** Lo que el cliente entrega según el tipo de operación. */
export function montoOperacion(
  tipo: TipoOperacion,
  precio: number,
  configuracion: Configuracion,
  montoReserva: number | null,
): number | null {
  switch (tipo) {
    case EstadoTransaccion.SENIADO:
      return Math.round(precio * configuracion.porcentaje_senia) / 100;
    case EstadoTransaccion.VENDIDO:
      return precio;
    default:
      return montoReserva;
  }
}

/** La reserva es el único monto que se escribe a mano, así que es el único que se valida. */
export function errorMontoReserva(
  tipo: TipoOperacion,
  precio: number,
  configuracion: Configuracion,
  montoReserva: number | null,
): string | null {
  if (tipo !== EstadoTransaccion.RESERVA) return null;
  if (!montoReserva || montoReserva <= 0) return 'Ingresá el precio acordado de la reserva.';

  const maximo = maximoReserva(precio, configuracion);
  if (montoReserva > maximo) {
    return `La reserva no puede superar el ${configuracion.porcentaje_maximo_reserva}% del precio ($ ${formatearMonto(maximo)}).`;
  }
  return null;
}

export function datosOperacion(tipo: TipoOperacion, montoReserva: number | null): DatosOperacion {
  return {
    estadoTransaccion: tipo,
    monto_entregado: tipo === EstadoTransaccion.RESERVA ? montoReserva : null,
  };
}
