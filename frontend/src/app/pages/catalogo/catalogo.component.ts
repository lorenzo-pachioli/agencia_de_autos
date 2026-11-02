import { Component, computed, inject, signal } from '@angular/core';
import { MatGridListModule } from '@angular/material/grid-list';
import { VehiculoService } from '../../services/vehiculo-service.service';
import { FiltrosVehiculo, Pagina, Vehiculo } from '../../models/Vehiculo';
import { VehiculoCardComponent } from '../../components/vehiculo-card/vehiculo-card.component';
import { Router } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { FiltrosComponent } from '../../components/filtros/filtros.component';
import { toSignal } from '@angular/core/rxjs-interop';

@Component({
  imports: [MatGridListModule, VehiculoCardComponent, MatIconModule, FiltrosComponent],
  selector: 'app-catalogo',
  styleUrl: './catalogo.component.scss',
  templateUrl: './catalogo.component.html',
})
export class CatalogoComponent {
  private readonly vehiculosSrv = inject(VehiculoService);
  private readonly router = inject(Router);

  readonly tamanio = 10;

  readonly vehiculos = signal<Pagina<Vehiculo> | null>(null);
  readonly vehiculosList = computed(() => this.vehiculos()?.content);

  marcas = toSignal(this.vehiculosSrv.getMarcas(), { initialValue: [] });
  modelos = toSignal(this.vehiculosSrv.getModelos(), { initialValue: [] });
  combustibles = toSignal(this.vehiculosSrv.getCombustibles(), { initialValue: [] });
  transmisiones = toSignal(this.vehiculosSrv.getTransmisiones(), { initialValue: [] });

  filtros = signal<FiltrosVehiculo>({});
  page = signal(0);

  constructor() {
    this.vehiculosSrv.obtenerTodos(0, 9).subscribe({
      next: res => {
        this.vehiculos.set(res)
      },
      error: err => console.log(err)
    });
  }

  irDetalle(id: number) {
    this.router.navigate(['/vehiculos', id]);
  }

  onFiltrar(f: FiltrosVehiculo): void {
    this.filtros.set({ ...f });
    this.page.set(0);
    this.vehiculosSrv.obtenerTodos(0, 9, f).subscribe({
      next: res => {
        this.vehiculos.set(res)
      },
      error: err => console.log(err)
    });
  }
}
