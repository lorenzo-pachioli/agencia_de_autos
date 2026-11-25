import { Component, inject } from '@angular/core';
import { MatButton } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { Observable } from 'rxjs';
import { EnvioDeModal } from '../../utils/modales';

export interface ConfirmarDialogData {
  titulo: string;
  mensaje: string;
  accion: string;
  ejecutar: () => Observable<unknown>;
  errorPorDefecto?: string;
}

@Component({
  selector: 'app-confirmar-dialog',
  imports: [MatDialogModule, MatButton],
  templateUrl: './confirmar-dialog.component.html',
})
export class ConfirmarDialogComponent {
  protected readonly data = inject<ConfirmarDialogData>(MAT_DIALOG_DATA);
  private readonly dialogRef = inject<MatDialogRef<ConfirmarDialogComponent, boolean>>(MatDialogRef);

  protected readonly envio = new EnvioDeModal<boolean>(this.dialogRef);

  protected confirmar(): void {
    this.envio.enviar(this.data.ejecutar(), true, this.data.errorPorDefecto);
  }

  protected cerrar(): void {
    this.dialogRef.close();
  }
}
