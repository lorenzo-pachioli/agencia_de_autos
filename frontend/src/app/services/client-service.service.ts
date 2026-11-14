import { HttpClient } from '@angular/common/http';
import { inject, Service } from '@angular/core';
import Vehicle from '../models/vehicle-model';
import VehicleResponse from '../models/vehicle-response-model';

@Service()
export class ClientServiceService {
  private http : HttpClient = inject(HttpClient)

  readonly URL = 'http://localhost:8080/vehiculos'

  getVehicles(){
    return this.http.get<Vehicle[]>(this.URL)
  }

  getVehicleById(id: number){
    return this.http.get<VehicleResponse>(this.URL + '/' + id)
  }

  putVehicle(vehicle : Vehicle){
    return this.http.put(this.URL + '/' + vehicle.modeloId,vehicle).subscribe(() => (
        alert("vehiculo modificado con exito")
    ))
  }

  // Devuelve el Observable: quien lo llame hace el .subscribe() y decide qué mostrar
  postVehicle(vehicle : Vehicle){
    return this.http.post(this.URL,vehicle)
  }

  // Devuelve el Observable: quien lo llame hace el .subscribe() y decide qué mostrar
  deleteVehicle(id: number){
    return this.http.delete(this.URL + '/' + id)
  }
}
