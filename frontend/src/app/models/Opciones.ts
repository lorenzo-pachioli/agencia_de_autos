import { TipoCombustible, TipoTransmision } from './Vehiculo';

export interface Marca {
  id: number;
  nombre: string;
  activo: boolean;
}

export type MarcaRequest = Omit<Marca, 'id'>;

export interface Modelo {
  id: number;
  nombre: string;
  marca: string;
}
