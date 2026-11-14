// Lo que devuelve el backend en GET /vehiculos/{id} (VehiculoResponseDTO).
// Ojo: no trae modeloId ni precioAdquisicion; trae el NOMBRE del modelo y una lista de imágenes.
export default interface VehicleResponse {
  id: number;
  patente: string;
  modeloNombre: string;
  anio: number;
  precioVenta: number;
  kilometraje: number;
  color: string;
  descripcion: string | null;
  estado: 'DISPONIBLE' | 'RESERVADO' | 'VENDIDO' | 'BAJA';
  tipoCombustible: 'NAFTA' | 'DIESEL' | 'HIBRIDO' | 'ELECTRICO';
  tipoTransmision: 'MANUAL' | 'AUTOMATICA';
  imagenPrincipalUrl: string | null;
  imagenes: string[];
  createdAt: string;
  updatedAt: string;
}
