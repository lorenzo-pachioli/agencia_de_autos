import { HttpClient } from '@angular/common/http';
import { inject, Service } from '@angular/core';
import { Observable } from 'rxjs';
import { Configuracion } from '../models/Configuracion';
import { CONFIGURACION_URL } from './api_config';

@Service()
export class ConfiguracionService {
  private readonly http = inject(HttpClient);

  obtener(): Observable<Configuracion> {
    return this.http.get<Configuracion>(CONFIGURACION_URL);
  }

  actualizar(porcentajeSenia: number): Observable<Configuracion> {
    return this.http.put<Configuracion>(CONFIGURACION_URL, { porcentaje_senia: porcentajeSenia });
  }
}
