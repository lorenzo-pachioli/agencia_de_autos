import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable, Service } from '@angular/core';
import { Observable } from 'rxjs';
import { TRANSACCIONES_URL } from './api_config';

export interface BalanceTransacciones {
  precios_final_total: number;
  comisiones_total: number;
  costos_vehiculos_vendidos: number;
  ingreso_final: number;
  fecha_desde: string;
  fecha_hasta: string;
}

@Service()
export class TransaccionService {
  private readonly http = inject(HttpClient);

  obtenerBalance(fechaDesde: string, fechaHasta: string): Observable<BalanceTransacciones> {
    const params = new HttpParams()
      .set('fechaDesde', fechaDesde)
      .set('fechaHasta', fechaHasta);

    return this.http.get<BalanceTransacciones>(`${TRANSACCIONES_URL}/rendimiento`, { params });
  }
}
