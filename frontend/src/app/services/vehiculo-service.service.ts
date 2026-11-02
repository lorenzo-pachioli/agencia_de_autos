import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { FiltrosVehiculo, Pagina, TipoCombustible, TipoTransmision, Vehiculo } from '../models/Vehiculo';
import { MARCAS_URL, MODELOS_URL, VEHICULOS_URL } from './api_config';
import { Marca, Modelo } from '../models/Opciones';


@Injectable({ providedIn: 'root' })
export class VehiculoService {
    private readonly http = inject(HttpClient);

    obtenerPorId(id: number): Observable<Vehiculo> {
        return this.http.get<Vehiculo>(`${VEHICULOS_URL}/${id}`);
    }

    obtenerTodos(
        pagina = 0,
        tamanio = 10,
        filtros: FiltrosVehiculo = {},
    ): Observable<Pagina<Vehiculo>> {
        const params: Record<string, number> = { page: pagina, size: tamanio };

        Object.entries(filtros).forEach(([clave, valor]) => {
            if (valor !== undefined && valor !== null && valor !== '') {
                params[clave] = valor;
            }
        });

        return this.http.get<Pagina<Vehiculo>>(`${VEHICULOS_URL}/busqueda`, { params });
    }

    getMarcas(): Observable<Marca[]> {
        return this.http.get<Marca[]>(`${MARCAS_URL}`);
    }

    getModelos(): Observable<Modelo[]> {
        return this.http.get<Modelo[]>(`${MODELOS_URL}`);
    }

    getCombustibles(): Observable<TipoCombustible[]> {
        return of(Object.values(TipoCombustible));
    }
    getTransmisiones(): Observable<TipoTransmision[]> {
        return of(Object.values(TipoTransmision));
    }
}

