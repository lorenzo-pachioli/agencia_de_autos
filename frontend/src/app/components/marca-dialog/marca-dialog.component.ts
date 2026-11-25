import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Observable } from 'rxjs';
import { EnvioDeModal } from '../../utils/modales';
import { MatButton } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { Marca, MarcaRequest } from '../../models/Opciones';

export interface MarcaDialogData {
  marca: Marca | null;
  guardar: (marca: MarcaRequest) => Observable<unknown>;
}

const MSG = {
  nombreObligatorio: 'Ingresá el nombre de la marca.',
};

@Component({
  selector: 'app-marca-dialog',
  imports: [FormsModule, MatDialogModule, MatFormFieldModule, MatInputModule, MatSlideToggleModule, MatButton],
  templateUrl: './marca-dialog.component.html',
})
export class MarcaDialogComponent {
  private readonly data = inject<MarcaDialogData>(MAT_DIALOG_DATA);
  private readonly dialogRef = inject<MatDialogRef<MarcaDialogComponent, MarcaRequest>>(MatDialogRef);

  protected readonly envio = new EnvioDeModal<MarcaRequest>(this.dialogRef);
  protected readonly marca = this.data.marca;
  protected readonly esEdicion = this.marca !== null;
  protected nombre = this.marca?.nombre ?? '';
  protected activo = this.marca?.activo ?? true;

  protected confirmar(): void {
    const nombre = this.nombre.trim();
    if (!nombre) {
      this.envio.error.set(MSG.nombreObligatorio);
      return;
    }
    const marca: MarcaRequest = { nombre, activo: this.activo };
    this.envio.enviar(this.data.guardar(marca), marca);
  }

  protected cerrar(): void {
    this.dialogRef.close();
  }
}
