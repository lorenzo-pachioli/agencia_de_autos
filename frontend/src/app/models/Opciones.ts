import { TipoCombustible, TipoTransmision } from './Vehiculo';

export interface Marca {
  id: number;
  nombre: string;
  activo: boolean;
}

export type MarcaRequest = Omit<Marca, 'id'>;

export enum TipoModelo {
  AUTO = 'AUTO',
  MOTO = 'MOTO',
  CAMIONETA = 'CAMIONETA',
  SUV = 'SUV',
  UTILITARIO = 'UTILITARIO',
}

export const ETIQUETA_TIPO_MODELO: Record<TipoModelo, string> = {
  [TipoModelo.AUTO]: 'Auto',
  [TipoModelo.MOTO]: 'Moto',
  [TipoModelo.CAMIONETA]: 'Camioneta',
  [TipoModelo.SUV]: 'SUV',
  [TipoModelo.UTILITARIO]: 'Utilitario',
};

export interface Modelo {
  id: number;
  nombre: string;
  marca: string;
  marcaId: number;
  anio: number;
  tipoModelo: TipoModelo;
  activo: boolean;
}

export type ModeloRequest = Omit<Modelo, 'id' | 'marca'>;
