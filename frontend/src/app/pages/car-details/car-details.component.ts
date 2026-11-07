import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ClientServiceService } from '../../services/client-service.service';
import VehicleModel from '../../models/vehicle-model';

@Component({
  imports: [],
  selector: 'app-car-details',
  styleUrl: './car-details.component.scss',
  templateUrl: './car-details.component.html',
})
export class CarDetailsComponent implements OnInit {
  private carService = inject(ClientServiceService)
  private route = inject(ActivatedRoute)

  private carId = Number(this.route.snapshot.params['id'])
  private autoSeleccionado? : VehicleModel

  ngOnInit(){
    this.traerAutoSeleccionado()
  }

  traerAutoSeleccionado() {

    this.carService.getVehicleById(this.carId).subscribe((csl : any) => {
      this.autoSeleccionado = csl;
    });
  }

  borrarAuto(){
    this.carService.deleteVehicle(Number(this.carId)).subscribe({
      next: () => {alert("vehiculo eliminado con exito")}
    })
  }

  modificarAuto(){
    const autoSeleccionado = this.autoSeleccionado
    if (!autoSeleccionado) return

    this.carService.putVehicle(autoSeleccionado).subscribe({
      next: () => {alert("vehiculo modificado con exito")}
    })
  }
} 
