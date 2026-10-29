import { Component, inject } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ClientServiceService } from '../../services/client-service.service';

@Component({
  imports: [ReactiveFormsModule],
  selector: 'app-form-client',
  styleUrl: './form-client.component.scss',
  templateUrl: './form-client.component.html',
})
export class FormClientComponent {

  private clientService = inject(ClientServiceService)
  
  formularioAutos: FormGroup
  marcaId: FormControl
  modeloId: FormControl
  anio: FormControl
  kilometraje: FormControl
  precioVenta: FormControl
  combustible: FormControl
  transmision: FormControl
  color: FormControl
  descripcion: FormControl

  opcionesCombustible = ['Nafta', 'Diesel', 'GNC', 'Eléctrico']
  opcionesTransmicion = ['Manual', 'Automatico']
  opcionesMarcaId = [1,2,3,4] // harcodeado con fines de prueba
  opcionesModeloId = [1,2,3] // harcodeado con fines de prueba


  constructor(){
    this.marcaId = new FormControl('')
    this.modeloId = new FormControl('')
    this.anio = new FormControl('')
    this.kilometraje = new FormControl('')
    this.precioVenta = new FormControl('')
    this.combustible = new FormControl('')
    this.transmision = new FormControl('')
    this.color = new FormControl('')
    this.descripcion = new FormControl('')
    this.formularioAutos = new FormGroup({
      marcaIdf: this.marcaId,
      modeloIdf: this.modeloId,
      aniof: this.anio,
      kilometrajef: this.kilometraje,
      precioVentaf: this.precioVenta,
      combustiblef: this.combustible,
      transmisionf: this.transmision,
      colorf: this.color,
      descripcionf: this.descripcion
    })
  }

  setMarca(valor: number) {
    this.marcaId.setValue(valor)
  }

  setModelo(valor: number) {
    this.modeloId.setValue(valor)
  }

  setCombustible(valor: string) {
    this.combustible.setValue(valor)
  }

  setTransmision(valor: string) {
    this.transmision.setValue(valor)
  }

  manejarEnvios(){
    return this.clientService.postVehicle(this.formularioAutos.value).subscribe({
      next: (dato: any) => {console.log(dato),alert("vehiculo cargado con exito"),this.formularioAutos.reset()},
    })
  }
}