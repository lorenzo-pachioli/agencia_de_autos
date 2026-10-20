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
}

export enum EstadoVehiculo {
  DISPONIBLE = 'DISPONIBLE',
  RESERVADO = 'RESERVADO',
  VENDIDO = 'VENDIDO',
  BAJA = 'BAJA',
}

export enum TipoCombustible {
  NAFTA = 'NAFTA',
  DIESEL = 'DIESEL',
  ELECTRICO = 'ELECTRICO',
  HIBRIDO = 'HIBRIDO',
  GNC = 'GNC',
}

export enum TipoTransmision {
  MANUAL = 'MANUAL',
  AUTOMATICA = 'AUTOMATICA',
  SEMIAUTOMATICA = 'SEMIAUTOMATICA',
}

export interface FiltrosVehiculo {
  marcaId?: number;
  modeloId?: number;
  combustible?: TipoCombustible;
  tipoTransmision?: TipoTransmision;
  estado?: EstadoVehiculo;
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