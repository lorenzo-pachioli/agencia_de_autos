import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { FiltrosVehiculo, Pagina, Vehiculo } from '../models/Vehiculo';
import { VEHICULOS_URL } from './api_config';


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
}

