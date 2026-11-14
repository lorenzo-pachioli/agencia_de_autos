import { HttpClient } from '@angular/common/http';
import { inject, Service } from '@angular/core';
import { Observable } from 'rxjs';
import { Modelo, ModeloRequest } from '../models/Opciones';
import { MODELOS_URL } from './api_config';

@Service()
export class ModeloService {
  private readonly http = inject(HttpClient);

  listar(): Observable<Modelo[]> {
    return this.http.get<Modelo[]>(MODELOS_URL);
  }

  crear(modelo: ModeloRequest): Observable<Modelo> {
    return this.http.post<Modelo>(MODELOS_URL, modelo);
  }

  actualizar(id: number, modelo: ModeloRequest): Observable<Modelo> {
    return this.http.put<Modelo>(`${MODELOS_URL}/${id}`, modelo);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${MODELOS_URL}/${id}`);
  }
}
