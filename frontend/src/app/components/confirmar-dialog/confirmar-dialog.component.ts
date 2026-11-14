import { Component, inject } from '@angular/core';
import { MatButton } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';

export interface ConfirmarDialogData {
  titulo: string;
  mensaje: string;
  accion: string;
}

@Component({
  selector: 'app-confirmar-dialog',
  imports: [MatDialogModule, MatButton],
  templateUrl: './confirmar-dialog.component.html',
})
export class ConfirmarDialogComponent {
  protected readonly data = inject<ConfirmarDialogData>(MAT_DIALOG_DATA);
  private readonly dialogRef = inject<MatDialogRef<ConfirmarDialogComponent, boolean>>(MatDialogRef);

  protected confirmar(): void {
    this.dialogRef.close(true);
  }

  protected cerrar(): void {
    this.dialogRef.close();
  }
}
