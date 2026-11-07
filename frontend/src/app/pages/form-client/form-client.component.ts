import { Component, computed, effect, inject, input } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButton } from '@angular/material/button';
import { map } from 'rxjs';
import Vehicle from '../../models/vehicle-model';
import { ClientServiceService } from '../../services/client-service.service';

// Patente argentina: vieja (ABC123) o Mercosur (AB123CD)
const PATENTE_REGEX = /^([A-Za-z]{3}\d{3}|[A-Za-z]{2}\d{3}[A-Za-z]{2})$/;

@Component({
  imports: [ReactiveFormsModule, MatButton],
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
    this.formularioAutos.statusChanges.pipe(map((estado : any) => estado === 'VALID')),
    { initialValue: this.formularioAutos.valid },
  )
  incompleto = computed(() => !this.valido())

  constructor() {
    // Si el padre manda un vehículo, se precargan los campos
    effect(() => {
      const v = this.vehiculo()
      if (v) {
        this.formularioAutos.patchValue({
          patente: v.patente,
          modeloId: v.modeloId,
          anio: v.anio,
          kilometraje: v.kilometraje,
          precioAdquisicion: v.precioAdquisicion,
          precioVenta: v.precioVenta,
          tipoCombustible: v.tipoCombustible,
          tipoTransmision: v.tipoTransmision,
          color: v.color,
          descripcion: v.descripcion ?? '',
        })
      }
    })
  }

  // Devuelve el vehículo listo para mandar al backend, o null si el form está incompleto
  obtenerVehiculo(): Vehicle | null {
    if (this.formularioAutos.invalid) return null
    const v = this.formularioAutos.getRawValue()
    return {
      patente: v.patente.trim().toUpperCase(),
      anio: v.anio!,
      precioAdquisicion: v.precioAdquisicion!,
      precioVenta: v.precioVenta!,
      modeloId: v.modeloId!,
      kilometraje: v.kilometraje!,
      color: v.color.trim(),
      descripcion: v.descripcion.trim(),
      estado: this.vehiculo()?.estado ?? 'DISPONIBLE', // un vehículo nuevo arranca disponible
      tipoTransmision: v.tipoTransmision!,
      tipoCombustible: v.tipoCombustible!,
    }
  }

  // Botón "Cargar vehículo": manda el vehículo a postVehicle()
  manejarEnvios() {
    if (!this.mostrarBoton()) return
    const vehiculo = this.obtenerVehiculo()
    if (!vehiculo) return

    this.clientService.postVehicle(vehiculo).subscribe({
      next: () => {
        alert('Vehículo cargado con éxito')
        this.formularioAutos.reset()
      },
      error: (e: { error?: { message?: string } }) =>
        alert(e.error?.message ?? 'No se pudo cargar el vehículo'),
    })
  }
}
