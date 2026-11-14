import { Component, computed, effect, inject, input, signal } from '@angular/core';
import { VehiculoService } from '../../services/vehiculo-service.service';
import { ETIQUETA_ESTADO_VEHICULO, EstadoVehiculo, Vehiculo } from '../../models/Vehiculo';
import { RouterLink } from '@angular/router';
import { MatIcon } from '@angular/material/icon';
import { CurrencyPipe } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import type { HttpErrorResponse } from '@angular/common/http';
import { AuthService } from '../../services/auth.service';
import { TransaccionService } from '../../services/transaccion.service';import type { ApiError } from '../../models/ApiError';

@Component({
  imports: [RouterLink, MatIcon, CurrencyPipe, MatButtonModule],
  selector: 'app-vehiculo-detalle',
  styleUrl: './vehiculo-detalle.component.scss',
  templateUrl: './vehiculo-detalle.component.html',
})
export class VehiculoDetalleComponent {
  private readonly vehiculoSrv = inject(VehiculoService);
  private readonly transaccionSrv = inject(TransaccionService);
  private readonly auth = inject(AuthService);

  protected readonly EstadoVehiculo = EstadoVehiculo;
  protected readonly etiquetaEstado = ETIQUETA_ESTADO_VEHICULO;

  id = input.required<string>();
  logged = this.auth.isLoggedIn;
  vehiculo = signal<Vehiculo | null>(null);
  indice = signal(0);

  enviandoSolicitud = signal(false);
  solicitudEnviada = signal(false);
  errorSolicitud = signal<string | null>(null);

  solicitarTurno() {
    const v = this.vehiculo();
    if (!v || this.enviandoSolicitud()) return;

    this.enviandoSolicitud.set(true);
    this.errorSolicitud.set(null);

    this.transaccionSrv.solicitar({ vehiculo_id: v.id }).subscribe({
      next: () => {
        this.enviandoSolicitud.set(false);
        this.solicitudEnviada.set(true);
      },
      error: (err: HttpErrorResponse) => {
        this.enviandoSolicitud.set(false);
        this.errorSolicitud.set((err.error as ApiError)?.message ?? 'No se pudo enviar la solicitud.');
      },
    });
  }

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
