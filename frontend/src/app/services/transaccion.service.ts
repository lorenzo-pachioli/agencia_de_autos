import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable, Service } from '@angular/core';
import { Observable } from 'rxjs';
import { Pagina } from '../models/Vehiculo';
import { TRANSACCIONES_URL } from './api_config';
import { BalanceTransacciones, CrearTransaccion, FiltrosTransaccion, Transaccion } from '../models/Transacciones';


@Service()
export class TransaccionService {
  private readonly http = inject(HttpClient);

  listar(pagina = 0, tamanio = 10, filtros: FiltrosTransaccion = {}): Observable<Pagina<Transaccion>> {
    let params = new HttpParams().set('page', pagina).set('size', tamanio).set('sort', 'id,asc');
    if (filtros.estadoTransaccion !== undefined) params = params.set('estadoTransaccion', filtros.estadoTransaccion);
    if (filtros.fechaDesde) params = params.set('fechaDesde', filtros.fechaDesde);
    if (filtros.fechaHasta) params = params.set('fechaHasta', filtros.fechaHasta);
    return this.http.get<Pagina<Transaccion>>(TRANSACCIONES_URL, { params });
  }

  obtenerPorId(id: number): Observable<Transaccion> {
    return this.http.get<Transaccion>(`${TRANSACCIONES_URL}/${id}`);
  }

  crear(transaccion: CrearTransaccion): Observable<Transaccion> {
    return this.http.post<Transaccion>(TRANSACCIONES_URL, transaccion);
  }

  senar(id: number, precioSenia: number): Observable<Transaccion> {
    const params = new HttpParams().set('precioSenia', precioSenia);
    return this.http.patch<Transaccion>(`${TRANSACCIONES_URL}/${id}/seniar`, null, { params });
  }

  vender(id: number, precioFinal: number): Observable<Transaccion> {
    const params = new HttpParams().set('precioFinal', precioFinal);
    return this.http.patch<Transaccion>(`${TRANSACCIONES_URL}/${id}/vender`, null, { params });
  }

  cancelar(id: number): Observable<Transaccion> {
    return this.http.patch<Transaccion>(`${TRANSACCIONES_URL}/${id}/cancelar`, null);
  }

  obtenerBalance(fechaDesde: Date, fechaHasta: Date): Observable<BalanceTransacciones> {
    const params = new HttpParams()
      .set('fechaDesde', this.formatearFecha(fechaDesde))
      .set('fechaHasta', this.formatearFecha(fechaHasta));

    return this.http.get<BalanceTransacciones>(`${TRANSACCIONES_URL}/rendimiento`, { params });
  }

  formatearFecha(fecha: Date): string {
    const anio = fecha.getFullYear();
    const mes = String(fecha.getMonth() + 1).padStart(2, '0');
    const dia = String(fecha.getDate()).padStart(2, '0');
    return `${anio}-${mes}-${dia}`;
  }
}
