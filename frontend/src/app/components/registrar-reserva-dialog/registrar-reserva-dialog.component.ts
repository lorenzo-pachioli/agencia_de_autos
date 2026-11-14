import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButton } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { ETIQUETA_METODO_PAGO, EstadoTransaccion, MetodoPago, Transaccion } from '../../models/Transacciones';

export type EstadoReserva = EstadoTransaccion.RESERVA | EstadoTransaccion.SENIADO;

export interface DatosReserva {
  metodoPago: MetodoPago;
  precio_final: number;
  estadoTransaccion: EstadoReserva;
}

const MSG = {
  precioInvalido: 'Ingresá un precio mayor a cero.',
};

@Component({
  selector: 'app-registrar-reserva-dialog',
  imports: [FormsModule, MatDialogModule, MatFormFieldModule, MatInputModule, MatSelectModule, MatButton],
  templateUrl: './registrar-reserva-dialog.component.html',
})
export class RegistrarReservaDialogComponent {
  protected readonly transaccion = inject<Transaccion>(MAT_DIALOG_DATA);
  private readonly dialogRef = inject<MatDialogRef<RegistrarReservaDialogComponent, DatosReserva>>(MatDialogRef);

  protected readonly EstadoTransaccion = EstadoTransaccion;
  protected readonly metodosPago = Object.values(MetodoPago);
  protected readonly etiquetaMetodoPago = ETIQUETA_METODO_PAGO;
  protected readonly error = signal('');
  protected metodoPago: MetodoPago = this.transaccion.metodoPago ?? MetodoPago.EFECTIVO;
  protected precio: number | null = this.transaccion.precio_final;

  protected confirmar(estadoTransaccion: EstadoReserva): void {
    if (!this.precio || this.precio <= 0) {
      this.error.set(MSG.precioInvalido);
      return;
    }
    this.dialogRef.close({ metodoPago: this.metodoPago, precio_final: this.precio, estadoTransaccion });
  }

  protected cerrar(): void {
    this.dialogRef.close();
  }
}
