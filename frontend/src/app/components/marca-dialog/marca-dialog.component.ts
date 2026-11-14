import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButton } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { Marca, MarcaRequest } from '../../models/Opciones';

const MSG = {
  nombreObligatorio: 'Ingresá el nombre de la marca.',
};

@Component({
  selector: 'app-marca-dialog',
  imports: [FormsModule, MatDialogModule, MatFormFieldModule, MatInputModule, MatSlideToggleModule, MatButton],
  templateUrl: './marca-dialog.component.html',
})
export class MarcaDialogComponent {
  protected readonly marca = inject<Marca | null>(MAT_DIALOG_DATA);
  private readonly dialogRef = inject<MatDialogRef<MarcaDialogComponent, MarcaRequest>>(MatDialogRef);

  protected readonly esEdicion = this.marca !== null;
  protected readonly error = signal('');
  protected nombre = this.marca?.nombre ?? '';
  protected activo = this.marca?.activo ?? true;

  protected confirmar(): void {
    const nombre = this.nombre.trim();
    if (!nombre) {
      this.error.set(MSG.nombreObligatorio);
      return;
    }
    this.dialogRef.close({ nombre, activo: this.activo });
  }

  protected cerrar(): void {
    this.dialogRef.close();
  }
}
