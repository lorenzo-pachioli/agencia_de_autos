import { HttpErrorResponse } from '@angular/common/http';
import { Component, computed, effect, inject, input } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButton } from '@angular/material/button';
import { MatError, MatFormField, MatLabel } from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';
import { MatOption, MatSelect } from '@angular/material/select';
import { map } from 'rxjs';
import Vehicle from '../../models/vehicle-model';
import { ClientServiceService } from '../../services/client-service.service';

// Patente argentina: vieja (ABC123) o Mercosur (AB123CD)
const PATENTE_REGEX = /^([A-Za-z]{3}\d{3}|[A-Za-z]{2}\d{3}[A-Za-z]{2})$/;

@Component({
  imports: [ReactiveFormsModule, MatButton, MatFormField, MatLabel, MatError, MatInput, MatSelect, MatOption],
  selector: 'app-form-client',
  styleUrl: './form-client.component.scss',
  standalone: true,
  templateUrl: './form-client.component.html',
})
export class FormClientComponent {
  private clientService = inject(ClientServiceService)
  private fb = inject(NonNullableFormBuilder)

  // ── Entradas (para reutilizar el formulario desde otro componente) ──────────
  // Datos para precargar el form (por ejemplo, en la pantalla de detalles para modificar)
  vehiculo = input<Partial<Vehicle> | null>(null)
  // true: el form muestra su propio botón y carga el vehículo (POST).
  // false: no muestra botón; el componente padre pone los suyos y usa obtenerVehiculo()
  mostrarBoton = input(true)

  // value = lo que espera el backend, label = lo que ve el usuario
  opcionesCombustible = [
    { value: 'NAFTA', label: 'Nafta' },
    { value: 'DIESEL', label: 'Diesel' },
    { value: 'HIBRIDO', label: 'Híbrido' },
    { value: 'ELECTRICO', label: 'Eléctrico' },
  ] as const
  opcionesTransmision = [
    { value: 'MANUAL', label: 'Manual' },
    { value: 'AUTOMATICA', label: 'Automática' },
  ] as const
  opcionesMarcaId = [1, 2, 3, 4] // harcodeado con fines de prueba
  opcionesModeloId = [1, 2, 3] // harcodeado con fines de prueba

  // Las claves coinciden con el modelo Vehicle (salvo marcaId, que es solo del formulario)
  formularioAutos = this.fb.group({
    patente: ['', [Validators.required, Validators.pattern(PATENTE_REGEX)]],
    marcaId: this.fb.control<number | null>(null, Validators.required),
    modeloId: this.fb.control<number | null>(null, Validators.required),
    anio: this.fb.control<number | null>(null, [
      Validators.required,
      Validators.min(1900),
      Validators.max(new Date().getFullYear() + 1),
    ]),
    kilometraje: this.fb.control<number | null>(null, [Validators.required, Validators.min(0)]),
    precioAdquisicion: this.fb.control<number | null>(null, [Validators.required, Validators.min(0)]),
    precioVenta: this.fb.control<number | null>(null, [Validators.required, Validators.min(0)]),
    tipoCombustible: this.fb.control<Vehicle['tipoCombustible'] | null>(null, Validators.required),
    tipoTransmision: this.fb.control<Vehicle['tipoTransmision'] | null>(null, Validators.required),
    color: ['', Validators.required],
    descripcion: [''], // opcional: el backend la acepta vacía
  })

  // true cuando el formulario está completo y es válido (el padre puede usarlo en su [disabled])
  valido = toSignal(
    this.formularioAutos.statusChanges.pipe(map((estadoDelFormulario) => estadoDelFormulario === 'VALID')),
    { initialValue: this.formularioAutos.valid },
  )
  incompleto = computed(() => !this.valido())

  constructor() {
    // Si el padre manda un vehículo, se precargan los campos
    effect(() => {
      const vehiculoPrecargado = this.vehiculo()
      if (vehiculoPrecargado) {
        this.formularioAutos.patchValue({
          patente: vehiculoPrecargado.patente,
          modeloId: vehiculoPrecargado.modeloId,
          anio: vehiculoPrecargado.anio,
          kilometraje: vehiculoPrecargado.kilometraje,
          precioAdquisicion: vehiculoPrecargado.precioAdquisicion,
          precioVenta: vehiculoPrecargado.precioVenta,
          tipoCombustible: vehiculoPrecargado.tipoCombustible,
          tipoTransmision: vehiculoPrecargado.tipoTransmision,
          color: vehiculoPrecargado.color,
          descripcion: vehiculoPrecargado.descripcion ?? '',
        })
      }
    })
  }

  // Devuelve el vehículo listo para mandar al backend, o null si el form está incompleto
  obtenerVehiculo(): Vehicle | null {
    if (this.formularioAutos.invalid) return null
    const valoresDelFormulario = this.formularioAutos.getRawValue()
    return {
      patente: valoresDelFormulario.patente.trim().toUpperCase(),
      anio: valoresDelFormulario.anio!,
      precioAdquisicion: valoresDelFormulario.precioAdquisicion!,
      precioVenta: valoresDelFormulario.precioVenta!,
      modeloId: valoresDelFormulario.modeloId!,
      kilometraje: valoresDelFormulario.kilometraje!,
      color: valoresDelFormulario.color.trim(),
      descripcion: valoresDelFormulario.descripcion.trim(),
      estado: this.vehiculo()?.estado ?? 'DISPONIBLE', // un vehículo nuevo arranca disponible
      tipoTransmision: valoresDelFormulario.tipoTransmision!,
      tipoCombustible: valoresDelFormulario.tipoCombustible!,
    }
  }

  // Botón "Cargar vehículo": manda el vehículo a postVehicle()
  manejarEnvios() {
    if (!this.mostrarBoton()) return
    const vehiculoParaEnviar = this.obtenerVehiculo()
    if (!vehiculoParaEnviar) return

    this.clientService.postVehicle(vehiculoParaEnviar).subscribe({
      next: () => {
        alert('Vehículo cargado con éxito')
        this.formularioAutos.reset()
      },
      error: (errorDelServidor: HttpErrorResponse) =>
        alert(errorDelServidor.error?.message ?? 'No se pudo cargar el vehículo'),
    })
  }

  // Texto que se muestra debajo de un campo inválido (<mat-error>)
  obtenerMensajeDeError(nombreDelCampo: keyof FormClientComponent['formularioAutos']['controls']): string {
    const erroresDelCampo = this.formularioAutos.controls[nombreDelCampo].errors
    if (!erroresDelCampo) return ''
    if (erroresDelCampo['required']) return 'Campo obligatorio'
    if (erroresDelCampo['pattern']) return 'Formato inválido (ej: AB123CD o ABC123)'
    if (erroresDelCampo['min']) return `El mínimo es ${erroresDelCampo['min'].min}`
    if (erroresDelCampo['max']) return `El máximo es ${erroresDelCampo['max'].max}`
    return 'Valor inválido'
  }
}
