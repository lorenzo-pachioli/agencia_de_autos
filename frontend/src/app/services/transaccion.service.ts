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

  obtenerBalance(fechaDesde: Date, fechaHasta: Date): Observable<BalanceTransacciones> {
    const params = new HttpParams()
      .set('fechaDesde', this.formatearFecha(fechaDesde))
      .set('fechaHasta', this.formatearFecha(fechaHasta));

    return this.http.get<BalanceTransacciones>(`${TRANSACCIONES_URL}/rendimiento`, { params });
  }

  private formatearFecha(fecha: Date): string {
    const anio = fecha.getFullYear();
    const mes = String(fecha.getMonth() + 1).padStart(2, '0');
    const dia = String(fecha.getDate()).padStart(2, '0');
    return `${anio}-${mes}-${dia}`;
  }
}
