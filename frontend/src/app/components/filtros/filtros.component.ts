import { Component, computed, inject, input, output } from '@angular/core';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { FiltrosVehiculo, TipoCombustible, TipoTransmision } from '../../models/Vehiculo';
import { Marca, Modelo } from '../../models/Opciones';

@Component({
  selector: 'app-filtros',
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatSelectModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
  ],
  styleUrl: './filtros.component.scss',
  templateUrl: './filtros.component.html',
})
export class FiltrosComponent {
  private readonly fb = inject(FormBuilder);


  marcas = input<Marca[]>([]);
  modelos = input<Modelo[]>([]);
  combustibles = input<TipoCombustible[]>([]);
  transmisiones = input<TipoTransmision[]>([]);

  filtrosChange = output<FiltrosVehiculo>();

  formFilters = this.fb.group({
    marcaId: null as number | null,
    modeloId: null as number | null,
    combustible: null as TipoCombustible | null,
    tipoTransmision: null as TipoTransmision | null,
    minPrecio: null as number | null,
    maxPrecio: null as number | null,
  });

  private readonly marcaSeleccionada = toSignal(
    this.formFilters.controls.marcaId.valueChanges,
    { initialValue: null },
  );

  modelosVisibles = computed(() => {
    const marcaId = this.marcaSeleccionada();
    const todos = this.modelos();
    if (marcaId == null) return todos;
    const marcaNombre = this.marcas().find(m => m.id === marcaId)?.nombre;
    return marcaNombre ? todos.filter(m => m.marca === marcaNombre) : todos;
  });

  constructor() {
    this.formFilters.controls.marcaId.valueChanges
      .subscribe(() => this.formFilters.controls.modeloId.setValue(null));
  }

  aplicar(): void {
    const v = this.formFilters.getRawValue();

    this.filtrosChange.emit({
      marcaId: v.marcaId ?? undefined,
      modeloId: v.modeloId ?? undefined,
      combustible: v.combustible ?? undefined,
      tipoTransmision: v.tipoTransmision ?? undefined,
      minPrecio: v.minPrecio ?? undefined,
      maxPrecio: v.maxPrecio ?? undefined,
    });
  }

  limpiar(): void {
    this.formFilters.reset();
    this.aplicar();
  }
}