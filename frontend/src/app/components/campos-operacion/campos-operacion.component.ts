import { Component, computed, input, model } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { Configuracion } from '../../models/Configuracion';
import {
  ETIQUETA_TIPO_OPERACION,
  TIPOS_OPERACION,
  formatearMonto,
  maximoReserva,
  montoOperacion,
} from '../../models/Operacion';
import { EstadoTransaccion, TipoOperacion } from '../../models/Transacciones';

/** Precio, tipo de operación y monto entregado. Se usa al registrar y al crear una transacción. */
@Component({
  selector: 'app-campos-operacion',
  imports: [FormsModule, MatFormFieldModule, MatInputModule, MatSelectModule],
  templateUrl: './campos-operacion.component.html',
  styleUrl: './campos-operacion.component.scss',
})
export class CamposOperacionComponent {
  readonly precio = input.required<number>();
  readonly configuracion = input.required<Configuracion>();
  readonly tipo = model.required<TipoOperacion>();
  readonly montoReserva = model.required<number | null>();

  protected readonly EstadoTransaccion = EstadoTransaccion;
  protected readonly tipos = TIPOS_OPERACION;
  protected readonly etiquetaTipo = ETIQUETA_TIPO_OPERACION;
  protected readonly formatearMonto = formatearMonto;

  protected readonly montoMaximo = computed(() => maximoReserva(this.precio(), this.configuracion()));
  protected readonly monto = computed(
    () => montoOperacion(this.tipo(), this.precio(), this.configuracion(), this.montoReserva()) ?? 0,
  );
  protected readonly saldo = computed(() => Math.max(this.precio() - this.monto(), 0));
}
