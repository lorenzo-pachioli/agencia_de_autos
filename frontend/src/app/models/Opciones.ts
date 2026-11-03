import { TipoCombustible, TipoTransmision } from './Vehiculo';

export interface Marca {
  id: number;
  nombre: string;
}

export interface Modelo {
  id: number;
  nombre: string;
  marca: string;
}
