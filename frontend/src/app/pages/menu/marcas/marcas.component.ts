import { Component, inject, signal } from '@angular/core';
import { MatButton } from '@angular/material/button';
import { MatDialog } from '@angular/material/dialog';
import { MatTableModule } from '@angular/material/table';
import { Observable } from 'rxjs';
import {
  ConfirmarDialogComponent,
  ConfirmarDialogData,
} from '../../../components/confirmar-dialog/confirmar-dialog.component';
import { MarcaDialogComponent } from '../../../components/marca-dialog/marca-dialog.component';
import { Marca, MarcaRequest } from '../../../models/Opciones';
import { AuthService } from '../../../services/auth.service';
import { MarcaService } from '../../../services/marca.service';

const DURACION_MENSAJE_MS = 3500;

const TAMANIO_DIALOGO = { width: '480px', maxWidth: '92vw' };

const MSG = {
  errorCarga: 'No se pudieron cargar las marcas.',
  errorOperacion: 'No se pudo completar la operación. Revisá los datos e intentá nuevamente.',
  errorEliminar: 'No se pudo eliminar la marca. Verificá que no tenga modelos asociados.',
  creada: 'Marca creada.',
  actualizada: 'Marca actualizada.',
  eliminada: 'Marca eliminada.',
};

@Component({
  imports: [MatTableModule, MatButton],
  selector: 'app-marcas',
  styleUrl: './marcas.component.scss',
  templateUrl: './marcas.component.html',
})
export class MarcasComponent {
  private readonly marcaService = inject(MarcaService);
  private readonly dialog = inject(MatDialog);
  private readonly auth = inject(AuthService);

  protected readonly isAdmin = this.auth.isAdmin;
  protected readonly columnas = ['id', 'nombre', 'activo', 'acciones'];

  readonly marcas = signal<Marca[]>([]);
  readonly cargando = signal(false);
  readonly error = signal('');
  readonly mensaje = signal('');

  constructor() {
    this.cargar();
  }

  cargar(): void {
    this.cargando.set(true);
    this.marcaService.listar().subscribe({
      next: marcas => {
        this.marcas.set(marcas);
        this.cargando.set(false);
      },
      error: () => {
        this.error.set(MSG.errorCarga);
        this.cargando.set(false);
      },
    });
  }

  abrirCrear(): void {
    this.error.set('');
    this.dialog
      .open<MarcaDialogComponent, Marca | null, MarcaRequest>(MarcaDialogComponent, {
        ...TAMANIO_DIALOGO,
        data: null,
      })
      .afterClosed()
      .subscribe(datos => {
        if (!datos) return;
        this.ejecutar(this.marcaService.crear(datos), MSG.creada);
      });
  }

  abrirEditar(marca: Marca): void {
    this.error.set('');
    this.dialog
      .open<MarcaDialogComponent, Marca | null, MarcaRequest>(MarcaDialogComponent, {
        ...TAMANIO_DIALOGO,
        data: marca,
      })
      .afterClosed()
      .subscribe(datos => {
        if (!datos) return;
        this.ejecutar(this.marcaService.actualizar(marca.id, datos), MSG.actualizada);
      });
  }

  abrirBorrar(marca: Marca): void {
    this.error.set('');
    this.dialog
      .open<ConfirmarDialogComponent, ConfirmarDialogData, boolean>(ConfirmarDialogComponent, {
        ...TAMANIO_DIALOGO,
        data: {
          titulo: 'Eliminar marca',
          mensaje: `¿Seguro que querés eliminar la marca ${marca.nombre}? Esta acción no se puede deshacer.`,
          accion: 'Eliminar',
        },
      })
      .afterClosed()
      .subscribe(confirmado => {
        if (!confirmado) return;
        this.ejecutar(this.marcaService.eliminar(marca.id), MSG.eliminada, MSG.errorEliminar);
      });
  }

  private ejecutar(solicitud: Observable<unknown>, exito: string, fallo = MSG.errorOperacion): void {
    solicitud.subscribe({
      next: () => {
        this.mostrarMensaje(exito);
        this.cargar();
      },
      error: () => this.error.set(fallo),
    });
  }

  private mostrarMensaje(texto: string): void {
    this.mensaje.set(texto);
    setTimeout(() => this.mensaje.set(''), DURACION_MENSAJE_MS);
  }
}
