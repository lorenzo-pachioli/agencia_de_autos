import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButton } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { Observable } from 'rxjs';
import { Configuracion } from '../../models/Configuracion';
import { ETIQUETA_TIPO_OPERACION, datosOperacion, errorMontoReserva } from '../../models/Operacion';
import {
  DatosOperacion,
  ETIQUETA_METODO_PAGO,
  EstadoTransaccion,
  MetodoPago,
  TipoOperacion,
  Transaccion,
} from '../../models/Transacciones';
import { ERROR_CAMPOS, EnvioDeModal } from '../../utils/modales';
import { CamposOperacionComponent } from '../campos-operacion/campos-operacion.component';

export interface RegistrarReservaData {
  transaccion: Transaccion;
  configuracion: Configuracion;
  guardar: (datos: DatosReserva) => Observable<unknown>;
}

export interface DatosReserva extends DatosOperacion {
  metodoPago: MetodoPago;
}

@Component({
  selector: 'app-registrar-reserva-dialog',
  imports: [FormsModule, MatDialogModule, MatFormFieldModule, MatSelectModule, MatButton, CamposOperacionComponent],
  templateUrl: './registrar-reserva-dialog.component.html',
})
export class RegistrarReservaDialogComponent {
  protected readonly data = inject<RegistrarReservaData>(MAT_DIALOG_DATA);
  private readonly dialogRef = inject<MatDialogRef<RegistrarReservaDialogComponent, DatosReserva>>(MatDialogRef);

  protected readonly envio = new EnvioDeModal<DatosReserva>(this.dialogRef);
  protected readonly metodosPago = Object.values(MetodoPago);
  protected readonly etiquetaMetodoPago = ETIQUETA_METODO_PAGO;
  protected readonly etiquetaTipo = ETIQUETA_TIPO_OPERACION;
  protected readonly intentoEnviar = signal(false);
  protected readonly tipo = signal<TipoOperacion>(EstadoTransaccion.RESERVA);
  protected readonly montoReserva = signal<number | null>(null);
  protected metodoPago: MetodoPago = this.data.transaccion.metodoPago ?? MetodoPago.EFECTIVO;

  protected confirmar(): void {
    this.intentoEnviar.set(true);
    const { transaccion, configuracion } = this.data;
    if (errorMontoReserva(this.tipo(), transaccion.precio_venta, configuracion, this.montoReserva())) {
      this.envio.error.set(ERROR_CAMPOS);
      return;
    }
    const datos: DatosReserva = { metodoPago: this.metodoPago, ...datosOperacion(this.tipo(), this.montoReserva()) };
    this.envio.enviar(this.data.guardar(datos), datos);
  }

  protected cerrar(): void {
    this.dialogRef.close();
  }
}
