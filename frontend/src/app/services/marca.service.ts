import { HttpClient } from '@angular/common/http';
import { inject, Service } from '@angular/core';
import { Observable } from 'rxjs';
import { Marca, MarcaRequest } from '../models/Opciones';
import { MARCAS_URL } from './api_config';

@Service()
export class MarcaService {
  private readonly http = inject(HttpClient);

  listar(): Observable<Marca[]> {
    return this.http.get<Marca[]>(MARCAS_URL);
  }

  crear(marca: MarcaRequest): Observable<Marca> {
    return this.http.post<Marca>(MARCAS_URL, marca);
  }

  actualizar(id: number, marca: MarcaRequest): Observable<Marca> {
    return this.http.put<Marca>(`${MARCAS_URL}/${id}`, marca);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${MARCAS_URL}/${id}`);
  }
}
