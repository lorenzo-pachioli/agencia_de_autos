import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButton } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Transaccion } from '../../models/Transacciones';

export interface AccionMontoDialogData {
  tipo: 'senia' | 'venta';
  transaccion: Transaccion;
}

const MSG = {
  montoInvalido: 'Ingresá un monto mayor a cero.',
};

@Component({
  selector: 'app-accion-monto-dialog',
  imports: [FormsModule, MatDialogModule, MatFormFieldModule, MatInputModule, MatButton],
  templateUrl: './accion-monto-dialog.component.html',
})
export class AccionMontoDialogComponent {
  protected readonly data = inject<AccionMontoDialogData>(MAT_DIALOG_DATA);
  private readonly dialogRef = inject<MatDialogRef<AccionMontoDialogComponent, number>>(MatDialogRef);

  protected readonly esSenia = this.data.tipo === 'senia';
  protected readonly error = signal('');
  protected monto: number | null = this.data.transaccion.precio_final;

  protected confirmar(): void {
    if (!this.monto || this.monto <= 0) {
      this.error.set(MSG.montoInvalido);
      return;
    }
    this.dialogRef.close(this.monto);
  }

  protected cerrar(): void {
    this.dialogRef.close();
  }
}
