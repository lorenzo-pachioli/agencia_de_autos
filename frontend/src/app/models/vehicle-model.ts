export default interface Vehicle {
  patente: string;
  anio: number;
  precioAdquisicion: number;
  precioVenta: number;
  modeloId: number;
  kilometraje: number;
  color: string;
  descripcion: string;
  estado: 'DISPONIBLE' | 'RESERVADO' | 'VENDIDO' | 'BAJA';
  tipoTransmision: 'MANUAL' | 'AUTOMATICA';
  tipoCombustible: 'NAFTA' | 'DIESEL' | 'HIBRIDO' | 'ELECTRICO';
}