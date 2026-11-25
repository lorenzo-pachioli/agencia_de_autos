import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Observable } from 'rxjs';
import { EnvioDeModal } from '../../utils/modales';
import { MatButton } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { ETIQUETA_TIPO_MODELO, Marca, Modelo, ModeloRequest, TipoModelo } from '../../models/Opciones';

export interface ModeloDialogData {
  modelo: Modelo | null;
  marcas: Marca[];
  guardar: (modelo: ModeloRequest) => Observable<unknown>;
}

const ANIO_MINIMO = 1900;

const MSG = {
  camposObligatorios: 'Completá marca, nombre, año y tipo.',
  anioInvalido: `El año tiene que ser ${ANIO_MINIMO} o posterior.`,
};

@Component({
  selector: 'app-modelo-dialog',
  imports: [
    FormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatSlideToggleModule,
    MatButton,
  ],
  templateUrl: './modelo-dialog.component.html',
})
export class ModeloDialogComponent {
  protected readonly data = inject<ModeloDialogData>(MAT_DIALOG_DATA);
  private readonly dialogRef = inject<MatDialogRef<ModeloDialogComponent, ModeloRequest>>(MatDialogRef);

  protected readonly envio = new EnvioDeModal<ModeloRequest>(this.dialogRef);
  protected readonly esEdicion = this.data.modelo !== null;
  protected readonly anioMinimo = ANIO_MINIMO;
  protected readonly tiposModelo = Object.values(TipoModelo);
  protected readonly etiquetaTipoModelo = ETIQUETA_TIPO_MODELO;
  protected marcaId: number | null = this.data.modelo?.marcaId ?? null;
  protected nombre = this.data.modelo?.nombre ?? '';
  protected anio: number | null = this.data.modelo?.anio ?? null;
  protected tipoModelo: TipoModelo | null = this.data.modelo?.tipoModelo ?? null;
  protected activo = this.data.modelo?.activo ?? true;

  protected confirmar(): void {

    const nombre = this.nombre.trim();

    if (!this.marcaId || !nombre || !this.anio || !this.tipoModelo) {
      this.envio.error.set(MSG.camposObligatorios);
      return;
    }

    if (this.anio < ANIO_MINIMO) {
      this.envio.error.set(MSG.anioInvalido);
      return;
    }
    
    const modelo: ModeloRequest = {
      nombre,
      anio: this.anio,
      tipoModelo: this.tipoModelo,
      marcaId: this.marcaId,
      activo: this.activo,
    };
    this.envio.enviar(this.data.guardar(modelo), modelo);
  }

  protected cerrar(): void {
    this.dialogRef.close();
  }
}
