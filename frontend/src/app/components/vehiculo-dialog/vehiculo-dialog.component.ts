import { Component, computed, inject } from '@angular/core';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButton } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { Marca, Modelo } from '../../models/Opciones';
import {
  ETIQUETA_COMBUSTIBLE,
  ETIQUETA_TRANSMISION,
  EstadoVehiculo,
  TipoCombustible,
  TipoTransmision,
  VehiculoGestion,
  VehiculoRequest,
} from '../../models/Vehiculo';

export interface VehiculoDialogData {
  vehiculo: VehiculoGestion | null;
  marcas: Marca[];
  modelos: Modelo[];
}

// Patente argentina: vieja (ABC123) o Mercosur (AB123CD)
const PATENTE_REGEX = /^([A-Za-z]{3}\d{3}|[A-Za-z]{2}\d{3}[A-Za-z]{2})$/;

const ANIO_MINIMO = 1900;

const ANIO_MAXIMO = new Date().getFullYear() + 1;

const MSG = {
  obligatorio: 'Campo obligatorio',
  patenteInvalida: 'Formato inválido (ej: AB123CD o ABC123)',
  invalido: 'Valor inválido',
};

@Component({
  selector: 'app-vehiculo-dialog',
  imports: [ReactiveFormsModule, MatDialogModule, MatFormFieldModule, MatInputModule, MatSelectModule, MatButton],
  styleUrl: './vehiculo-dialog.component.scss',
  templateUrl: './vehiculo-dialog.component.html',
})
export class VehiculoDialogComponent {
  protected readonly data = inject<VehiculoDialogData>(MAT_DIALOG_DATA);
  private readonly dialogRef = inject<MatDialogRef<VehiculoDialogComponent, VehiculoRequest>>(MatDialogRef);
  private readonly fb = inject(NonNullableFormBuilder);

  private readonly vehiculo = this.data.vehiculo;

  protected readonly esEdicion = this.vehiculo !== null;
  protected readonly esVendido = this.vehiculo?.estado === EstadoVehiculo.VENDIDO;
  protected readonly combustibles = Object.values(TipoCombustible);
  protected readonly transmisiones = Object.values(TipoTransmision);
  protected readonly etiquetaCombustible = ETIQUETA_COMBUSTIBLE;
  protected readonly etiquetaTransmision = ETIQUETA_TRANSMISION;

  protected readonly formulario = this.fb.group({
    patente: [this.vehiculo?.patente ?? '', [Validators.required, Validators.pattern(PATENTE_REGEX)]],
    marcaId: this.fb.control<number | null>(this.vehiculo?.marcaId ?? null, Validators.required),
    modeloId: this.fb.control<number | null>(this.vehiculo?.modeloId ?? null, Validators.required),
    anio: this.fb.control<number | null>(this.vehiculo?.anio ?? null, [
      Validators.required,
      Validators.min(ANIO_MINIMO),
      Validators.max(ANIO_MAXIMO),
    ]),
    kilometraje: this.fb.control<number | null>(this.vehiculo?.kilometraje ?? null, [Validators.required, Validators.min(0)]),
    precioAdquisicion: this.fb.control<number | null>(
      { value: this.vehiculo?.precioAdquisicion ?? null, disabled: this.esVendido },
      [Validators.required, Validators.min(0)],
    ),
    precioVenta: this.fb.control<number | null>(this.vehiculo?.precioVenta ?? null, [Validators.required, Validators.min(0)]),
    tipoCombustible: this.fb.control<TipoCombustible | null>(this.vehiculo?.tipoCombustible ?? null, Validators.required),
    tipoTransmision: this.fb.control<TipoTransmision | null>(this.vehiculo?.tipoTransmision ?? null, Validators.required),
    color: [this.vehiculo?.color ?? '', Validators.required],
    descripcion: [this.vehiculo?.descripcion ?? ''],
  });

  private readonly marcaSeleccionada = toSignal(this.formulario.controls.marcaId.valueChanges, {
    initialValue: this.formulario.controls.marcaId.value,
  });

  protected readonly modelosDeMarca = computed(() =>
    this.data.modelos.filter(modelo => modelo.marcaId === this.marcaSeleccionada()),
  );

  constructor() {
    this.formulario.controls.marcaId.valueChanges
      .pipe(takeUntilDestroyed())
      .subscribe(() => this.formulario.controls.modeloId.setValue(null));
  }

  protected confirmar(): void {
    if (this.formulario.invalid) {
      this.formulario.markAllAsTouched();
      return;
    }

    const valores = this.formulario.getRawValue();
    this.dialogRef.close({
      patente: valores.patente.trim().toUpperCase(),
      anio: valores.anio!,
      precioAdquisicion: valores.precioAdquisicion!,
      precioVenta: valores.precioVenta!,
      modeloId: valores.modeloId!,
      kilometraje: valores.kilometraje!,
      color: valores.color.trim(),
      descripcion: valores.descripcion.trim(),
      estado: this.vehiculo?.estado ?? EstadoVehiculo.DISPONIBLE,
      tipoTransmision: valores.tipoTransmision!,
      tipoCombustible: valores.tipoCombustible!,
    });
  }

  protected cerrar(): void {
    this.dialogRef.close();
  }

  protected mensajeError(campo: keyof VehiculoDialogComponent['formulario']['controls']): string {
    const errores = this.formulario.controls[campo].errors;
    if (!errores) return '';
    if (errores['required']) return MSG.obligatorio;
    if (errores['pattern']) return MSG.patenteInvalida;
    if (errores['min']) return `El mínimo es ${errores['min'].min}`;
    if (errores['max']) return `El máximo es ${errores['max'].max}`;
    return MSG.invalido;
  }
}
