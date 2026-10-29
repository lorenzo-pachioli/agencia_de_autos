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

  private carId = this.route.snapshot.paramMap.get('id')
  private autoSeleccionado? : VehicleModel

  ngOnInit(){
    this.traerAutoSeleccionado()
  }

  traerAutoSeleccionado() {
    if (this.carId === null) return

    const carId = Number(this.carId)
    if (Number.isNaN(carId)) return

    this.carService.getVehicleById(carId).subscribe((csl : any) => {
      this.autoSeleccionado = csl;
    });
  }

  borrarAuto(){
    this.carService.deleteVehicle(Number(this.carId)).suscribe({
      next: () => {alert("vehiculo eliminado con exito")}
    })
  }

  modificarAuto(){
    if (!this.autoSeleccionado) return

    this.carService.putVehicle(this.autoSeleccionado).suscribe({
      next: () => {alert("vehiculo modificado con exito")}
    })
  }
} 
