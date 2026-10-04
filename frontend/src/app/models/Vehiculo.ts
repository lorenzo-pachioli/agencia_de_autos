
export interface VehiculoPublico {
  id: number;
  patente: string;
  marca: string;
  modeloNombre: string;
  anio: number;
  precioVenta: number;
  kilometraje: number;
  color: string;
  descripcion: string;
  tipoCombustible: COMBUSTIBLE;
  tipoTransmision: TRANSMISION;
  imagenPrincipalUrl: string;
  imagenes: string[];
  estado: ESTADOVEHICULO;
  createdAt: string;
  updatedAt: string;
}

export enum ESTADOVEHICULO {
  DISPONIBLE = "DISPONIBLE",
  RESERVADO = "RESERVADO",
  VENDIDO = "VENDIDO",
  BAJA = "BAJA",
}

export enum COMBUSTIBLE {
  NAFTA = "NAFTA",
  DIESEL = "DIESEL",
  ELECTRICO = "ELECTRICO",
  HIBRIDO = "HIBRIDO",
  GNC = "GNC",
}

export enum TRANSMISION {
  MANUAL = "MANUAL",
  AUTOMATICA = "AUTOMATICA",
  SEMIAUTOMATICA = "SEMIAUTOMATICA",
}
