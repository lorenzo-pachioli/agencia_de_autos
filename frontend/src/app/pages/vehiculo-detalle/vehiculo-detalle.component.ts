import { Component, computed, effect, inject, input, signal } from '@angular/core';
import { VehiculoService } from '../../services/vehiculo-service.service';
import { Vehiculo } from '../../models/Vehiculo';
import { RouterLink } from '@angular/router';
import { MatIcon } from '@angular/material/icon';
import { CurrencyPipe } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';

@Component({
  imports: [RouterLink, MatIcon, CurrencyPipe, MatButtonModule],
  selector: 'app-vehiculo-detalle',
  styleUrl: './vehiculo-detalle.component.scss',
  templateUrl: './vehiculo-detalle.component.html',
})
export class VehiculoDetalleComponent {
  private readonly vehiculoSrv = inject(VehiculoService);

  id = input.required<string>();
  logged = signal<boolean>(false);
  vehiculo = signal<Vehiculo | null>(null);
  indice = signal(0);

  imagenes = computed(() => {
    const v = this.vehiculo();
    if (!v) return [];
    // principal primero, sin duplicados
    return [...new Set([v.imagenPrincipalUrl, ...(v.imagenes ?? [])])].filter(Boolean);
  });

  imagenActual = computed(() => this.imagenes()[this.indice()]);

  siguiente() {
    const n = this.imagenes().length;
    if (n) this.indice.update(i => (i + 1) % n);
  }

  anterior() {
    const n = this.imagenes().length;
    if (n) this.indice.update(i => (i - 1 + n) % n);
  }

  constructor() {
    effect(() => {
      const idActual = this.id();
      console.log('ID cambiado:', idActual);

      if (idActual) {
        this.vehiculoSrv.obtenerPorId(idActual).subscribe({
          next: res => {
            console.log('Vehículo recuperado:', res);
            this.vehiculo.set(res);
          },
          error: err => console.error('Error al traer vehículo', err)
        });
      }
    });
  }
}
