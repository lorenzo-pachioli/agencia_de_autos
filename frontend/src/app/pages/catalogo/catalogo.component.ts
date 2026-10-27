import { Component, computed, inject, signal } from '@angular/core';
import { MatGridListModule } from '@angular/material/grid-list';
import { VehiculoService } from '../../services/vehiculo-service.service';
import { Pagina, Vehiculo } from '../../models/Vehiculo';
import { VehiculoCardComponent } from '../../components/vehiculo-card/vehiculo-card.component';
import { Router } from '@angular/router';

@Component({
  imports: [MatGridListModule, VehiculoCardComponent],
  selector: 'app-catalogo',
  styleUrl: './catalogo.component.scss',
  templateUrl: './catalogo.component.html',
})
export class CatalogoComponent {
  private readonly vehiculosSrv = inject(VehiculoService);
  private readonly router = inject(Router);
  readonly vehiculos = signal<Pagina<Vehiculo> | null>(null);
  readonly vehiculosList = computed(() => this.vehiculos()?.content)

  constructor(){
    this.vehiculosSrv.obtenerTodos(0, 9).subscribe({
      next: res => {
        console.log(res)
        this.vehiculos.set(res)
      },
      error: err => console.log(err)
    });
  }

  irDetalle(id: number){
    this.router.navigate(['/vehiculos', id]);
  }
}
