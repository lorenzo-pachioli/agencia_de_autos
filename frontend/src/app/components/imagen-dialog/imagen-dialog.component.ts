import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Observable } from 'rxjs';
import { EnvioDeModal } from '../../utils/modales';
import { MatButton } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { Imagen, ImagenRequest } from '../../models/Imagen';

export interface ImagenDialogData {
  imagen: Imagen | null;
  vehiculoId: number;
  guardar: (imagen: ImagenRequest) => Observable<unknown>;
}

const TAMANIO_MAXIMO_MB = 2;

const TAMANIO_MAXIMO_BYTES = TAMANIO_MAXIMO_MB * 1024 * 1024;

const MSG = {
  imagenObligatoria: 'Elegí una imagen.',
  imagenMuyGrande: `La imagen no puede superar los ${TAMANIO_MAXIMO_MB} MB.`,
};

@Component({
  selector: 'app-imagen-dialog',
  imports: [FormsModule, MatDialogModule, MatSlideToggleModule, MatButton],
  styleUrl: './imagen-dialog.component.scss',
  templateUrl: './imagen-dialog.component.html',
})
export class ImagenDialogComponent {
  protected readonly data = inject<ImagenDialogData>(MAT_DIALOG_DATA);
  private readonly dialogRef = inject<MatDialogRef<ImagenDialogComponent, ImagenRequest>>(MatDialogRef);

  protected readonly envio = new EnvioDeModal<ImagenRequest>(this.dialogRef);
  protected readonly esEdicion = this.data.imagen !== null;
  protected readonly url = signal(this.data.imagen?.url ?? '');
  protected esPrincipal = this.data.imagen?.esPrincipal ?? false;

  protected seleccionarArchivo(evento: Event): void {
    const archivo = (evento.target as HTMLInputElement).files?.[0];
    if (!archivo) return;

    if (archivo.size > TAMANIO_MAXIMO_BYTES) {
      this.envio.error.set(MSG.imagenMuyGrande);
      return;
    }

    this.envio.error.set('');
    const lector = new FileReader();
    lector.onload = () => this.url.set(lector.result as string);
    lector.readAsDataURL(archivo);
  }

  protected confirmar(): void {
    if (!this.url()) {
      this.envio.error.set(MSG.imagenObligatoria);
      return;
    }

    const imagen: ImagenRequest = {
      url: this.url(),
      esPrincipal: this.esPrincipal,
      vehiculoId: this.data.vehiculoId,
    };
    this.envio.enviar(this.data.guardar(imagen), imagen);
  }

  protected cerrar(): void {
    this.dialogRef.close();
  }
}
