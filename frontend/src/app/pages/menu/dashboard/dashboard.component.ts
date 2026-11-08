import { Component, computed, effect, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { toSignal } from '@angular/core/rxjs-interop';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { MAT_DATE_LOCALE, provideNativeDateAdapter } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { catchError, of } from 'rxjs';
import { DashboardCardComponent, DashboardCardData } from '../../../components/dashboard-card/dashboard-card.component';
import { AuthService } from '../../../services/auth.service';
import { BalanceTransacciones, TransaccionService } from '../../../services/transaccion.service';
import { VehiculoService } from '../../../services/vehiculo-service.service';
import { ReporteGananciasVehiculos, ReporteStockVehiculos } from '../../../models/Vehiculo';

@Component({
  imports: [DashboardCardComponent, DatePipe, ReactiveFormsModule, MatDatepickerModule, MatFormFieldModule, MatInputModule, MatButtonModule],
  providers: [{ provide: MAT_DATE_LOCALE, useValue: 'es-AR' }, provideNativeDateAdapter()],
  selector: 'app-dashboard',
  styleUrl: './dashboard.component.scss',
  templateUrl: './dashboard.component.html',
})
export class DashboardComponent {
  readonly auth = inject(AuthService);
  private readonly vehiculoService = inject(VehiculoService);
  private readonly transaccionService = inject(TransaccionService);
  readonly hoy = new Date();
  readonly periodo = signal({ desde: new Date(this.hoy.getFullYear(), this.hoy.getMonth(), 1), hasta: this.hoy });
  readonly fechaDesdeControl = new FormControl<Date | null>(this.periodo().desde);
  readonly fechaHastaControl = new FormControl<Date | null>(this.periodo().hasta);
  readonly autorizado = signal(this.auth.isAdmin());
  readonly cargando = signal(true);
  readonly cargandoBalance = signal(true);
  readonly error = signal(false);

  readonly stock = toSignal<ReporteStockVehiculos | null>(
    this.autorizado()
      ? this.vehiculoService.obtenerReporteStock().pipe(catchError(() => {
          this.error.set(true);
          return of(null);
        }))
      : of(null),
    { initialValue: null },
  );
  
  readonly ganancias = toSignal<ReporteGananciasVehiculos | null>(
    this.autorizado()
      ? this.vehiculoService.obtenerReporteGanancias().pipe(catchError(() => {
          this.error.set(true);
          return of(null);
        }))
      : of(null),
    { initialValue: null },
  );

  private readonly balanceInicial = toSignal(
    (this.autorizado()
      ? this.transaccionService.obtenerBalance(this.periodo().desde, this.periodo().hasta)
      : of(null)),
    { initialValue: null },
  );

  private readonly balanceFiltrado = signal<BalanceTransacciones | null>(null);
  readonly balanceConsulta = computed(() => this.balanceFiltrado() ?? this.balanceInicial());

  get tarjetasStock(): DashboardCardData[] {
    const datos = this.stock();
    if (!datos) return [];
    return [
      { titulo: 'Disponibles', valor: datos.disponibles, descripcion: 'Listos para la venta' },
      { titulo: 'Reservados', valor: datos.reservados, descripcion: 'En proceso de operación' },
      { titulo: 'Vendidos', valor: datos.vendidos, descripcion: 'Unidades vendidas' },
      { titulo: 'Dados de baja', valor: datos.baja, descripcion: 'Fuera del inventario activo' },
    ];
  }

  get tarjetasFinancieras(): DashboardCardData[] {
    const balance = this.balanceConsulta();
    if (!balance) return [];
    const tarjetas: DashboardCardData[] = [
      { titulo: 'Ingreso neto del período', valor: balance.ingreso_final, descripcion: 'Ventas menos costos y comisiones', moneda: true },
      { titulo: 'Ventas del período', valor: balance.precios_final_total, descripcion: 'Importe de las ventas concretadas', moneda: true },
      { titulo: 'Costos de vehículos', valor: balance.costos_vehiculos_vendidos, descripcion: 'Costo de adquisición de vendidos', moneda: true },
      { titulo: 'Comisiones', valor: balance.comisiones_total, descripcion: 'Comisiones generadas en el período', moneda: true },
    ];
    const ganancias = this.ganancias();
    if (ganancias) {
      tarjetas.push({
        titulo: 'Ganancia histórica',
        valor: ganancias.gananciaTotal,
        descripcion: 'Acumulada sobre vehículos vendidos',
        moneda: true,
      });
    }
    return tarjetas;
  }

  constructor() {
    if (!this.autorizado()) {
      this.cargando.set(false);
      this.cargandoBalance.set(false);
      return;
    }

    effect(() => {
      const stock = this.stock();
      const ganancias = this.ganancias();
      const balance = this.balanceInicial();
      const todosLosReportesListos = stock !== null || ganancias !== null || balance !== null;
      if (todosLosReportesListos || this.error()) {
        this.cargando.set(false);
        this.cargandoBalance.set(false);
      }
    });

    effect(() => {
      const stock = this.stock();
      const ganancias = this.ganancias();
      const balance = this.balanceInicial();
      if (stock && ganancias && balance) {
        this.balanceFiltrado.set(balance);
        this.cargando.set(false);
        this.cargandoBalance.set(false);
        this.error.set(false);
      }
    });

    effect(() => {
      const periodo = this.periodo();
      const inicial = new Date(this.hoy.getFullYear(), this.hoy.getMonth(), 1);
      if (periodo.desde.getTime() === inicial.getTime() && periodo.hasta.getTime() === this.hoy.getTime()) return;

      this.cargandoBalance.set(true);
      this.transaccionService.obtenerBalance(periodo.desde, periodo.hasta).pipe(
        catchError(() => {
          this.error.set(true);
          this.cargandoBalance.set(false);
          return of(null);
        }),
      ).subscribe((balance) => {
        if (balance) {
          this.balanceFiltrado.set(balance);
          this.error.set(false);
        }
        this.cargandoBalance.set(false);
      });
    });
  }

  aplicarPeriodo(): void {
    const desde = this.fechaDesdeControl.value;
    const hasta = this.fechaHastaControl.value;
    if (!desde || !hasta || desde > hasta || hasta > this.hoy) {
      this.error.set(true);
      return;
    }

    this.error.set(false);
    this.periodo.set({ desde, hasta });
  }

}
