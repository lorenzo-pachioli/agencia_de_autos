import { HttpClient } from '@angular/common/http';
import { inject, Service } from '@angular/core';
import Vehicle from '../models/vehicle-model';

@Service()
export class ClientServiceService {
  private http : HttpClient = inject(HttpClient)

  readonly URL = 'http://localhost:8080/vehiculos'

  postVehicle(vehicle : Vehicle){
    return this.http.post(this.URL,vehicle).subscribe(() => (
        alert("vehiculo cargado con exito")
    ))
  }
}
