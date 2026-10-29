import { HttpClient } from '@angular/common/http';
import { inject, Service } from '@angular/core';
import Vehicle from '../models/vehicle-model';

@Service()
export class ClientServiceService {
  private http : HttpClient = inject(HttpClient)

  readonly URL = 'http://localhost:8080/vehiculos'

  getVehicles(){
    return this.http.get<Vehicle[]>(this.URL)
  }

  getVehicleById(id: number){
    return this.http.get<Vehicle>(this.URL + '/' + id)
  }

  putVehicle(vehicle : Vehicle){
    return this.http.put(this.URL + '/' + vehicle.modeloId,vehicle).subscribe(() => (
        alert("vehiculo modificado con exito")
    ))
  }

  postVehicle(vehicle : Vehicle){
    return this.http.post(this.URL,vehicle).subscribe(() => (
        alert("vehiculo cargado con exito")
    ))
  }

  deleteVehicle(id: number){
    return this.http.delete(this.URL + '/' + id).subscribe(() => (
        alert("vehiculo eliminado con exito")
    ))
  }
}
