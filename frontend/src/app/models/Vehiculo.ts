export interface Vehiculo {
  id: number;
  patente: string;
  modeloNombre: string;
  anio: number;
  precioVenta: number;
  kilometraje: number;
  color: string;
  descripcion: string;
  estado: EstadoVehiculo;
  tipoCombustible: TipoCombustible;
  tipoTransmision: TipoTransmision;
  imagenPrincipalUrl: string | null;
  imagenes: string[];
  createdAt: string;
  updatedAt: string;
  tieneTransaccionActiva?: boolean;
  tieneSolicitudPendiente?: boolean;
  esPersonalAgencia?: boolean;
}

export enum EstadoVehiculo {
  DISPONIBLE = 'DISPONIBLE',
  RESERVADO = 'RESERVADO',
  VENDIDO = 'VENDIDO',
  BAJA = 'BAJA',
}

export const ETIQUETA_ESTADO_VEHICULO: Record<EstadoVehiculo, string> = {
  [EstadoVehiculo.DISPONIBLE]: 'Disponible',
  [EstadoVehiculo.RESERVADO]: 'Reservado',
  [EstadoVehiculo.VENDIDO]: 'Vendido',
  [EstadoVehiculo.BAJA]: 'No disponible',
};

export enum TipoCombustible {
  NAFTA = 'NAFTA',
  DIESEL = 'DIESEL',
  ELECTRICO = 'ELECTRICO',
  HIBRIDO = 'HIBRIDO',
}

export enum TipoTransmision {
  MANUAL = 'MANUAL',
  AUTOMATICA = 'AUTOMATICA',
}

export const ETIQUETA_COMBUSTIBLE: Record<TipoCombustible, string> = {
  [TipoCombustible.NAFTA]: 'Nafta',
  [TipoCombustible.DIESEL]: 'Diesel',
  [TipoCombustible.HIBRIDO]: 'Híbrido',
  [TipoCombustible.ELECTRICO]: 'Eléctrico',
};

export const ETIQUETA_TRANSMISION: Record<TipoTransmision, string> = {
  [TipoTransmision.MANUAL]: 'Manual',
  [TipoTransmision.AUTOMATICA]: 'Automática',
};

export interface VehiculoRequest {
  patente: string;
  anio: number;
  precioAdquisicion: number;
  precioVenta: number;
  modeloId: number;
  kilometraje: number;
  color: string;
  descripcion: string;
  estado: EstadoVehiculo;
  tipoTransmision: TipoTransmision;
  tipoCombustible: TipoCombustible;
}

export interface VehiculoGestion extends Omit<VehiculoRequest, 'descripcion'> {
  id: number;
  marcaId: number;
  marcaNombre: string;
  modeloNombre: string;
  descripcion: string | null;
}

export interface FiltrosVehiculo {
  marcaId?: number;
  modeloId?: number;
  combustible?: TipoCombustible;
  tipoTransmision?: TipoTransmision;
  estado?: EstadoVehiculo;
  incluirVendidos?: boolean;
  color?: string;
  minPrecio?: number;
  maxPrecio?: number;
  minAnio?: number;
  maxAnio?: number;
}

export interface Pagina<T> {
    content: T[];
    totalElements: number;
    totalPages: number;
    number: number;
    size: number;
    first: boolean;
    last: boolean;
    numberOfElements: number;
    empty: boolean;
}

export interface ReporteStockVehiculos {
    disponibles: number;
    reservados: number;
    vendidos: number;
    baja: number;
    total: number;
}

export interface ReporteGananciasVehiculos {
    totalInvertido: number;
    totalVenta: number;
    gananciaTotal: number;
}
