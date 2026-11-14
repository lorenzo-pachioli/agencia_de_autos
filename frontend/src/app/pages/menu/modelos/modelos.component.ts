import { Component, inject, signal } from '@angular/core';
import { MatButton } from '@angular/material/button';
import { MatDialog } from '@angular/material/dialog';
import { MatTableModule } from '@angular/material/table';
import { Observable } from 'rxjs';
import {
  ConfirmarDialogComponent,
  ConfirmarDialogData,
} from '../../../components/confirmar-dialog/confirmar-dialog.component';
import { ModeloDialogComponent, ModeloDialogData } from '../../../components/modelo-dialog/modelo-dialog.component';
import { ETIQUETA_TIPO_MODELO, Marca, Modelo, ModeloRequest } from '../../../models/Opciones';
import { AuthService } from '../../../services/auth.service';
import { MarcaService } from '../../../services/marca.service';
import { ModeloService } from '../../../services/modelo.service';

const DURACION_MENSAJE_MS = 3500;

const TAMANIO_DIALOGO = { width: '480px', maxWidth: '92vw' };

const MSG = {
  errorCarga: 'No se pudieron cargar los modelos.',
  errorCargaMarcas: 'No se pudieron cargar las marcas.',
  errorOperacion: 'No se pudo completar la operación. Revisá los datos e intentá nuevamente.',
  errorEliminar: 'No se pudo eliminar el modelo. Verificá que no tenga vehículos asociados.',
  creado: 'Modelo creado.',
  actualizado: 'Modelo actualizado.',
  eliminado: 'Modelo eliminado.',
};

@Component({
  imports: [MatTableModule, MatButton],
  selector: 'app-modelos',
  styleUrl: './modelos.component.scss',
  templateUrl: './modelos.component.html',
})
export class ModelosComponent {
  private readonly modeloService = inject(ModeloService);
  private readonly marcaService = inject(MarcaService);
  private readonly dialog = inject(MatDialog);
  private readonly auth = inject(AuthService);

  protected readonly isAdmin = this.auth.isAdmin;
  protected readonly columnas = ['id', 'nombre', 'marca', 'anio', 'tipo', 'activo', 'acciones'];

  readonly modelos = signal<Modelo[]>([]);
  readonly marcas = signal<Marca[]>([]);
  readonly cargando = signal(false);
  readonly error = signal('');
  readonly mensaje = signal('');

  constructor() {
    this.cargar();
    this.marcaService.listar().subscribe({
      next: marcas => this.marcas.set(marcas),
      error: () => this.error.set(MSG.errorCargaMarcas),
    });
  }

  cargar(): void {
    this.cargando.set(true);
    this.modeloService.listar().subscribe({
      next: modelos => {
        this.modelos.set(modelos);
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
      .open<ModeloDialogComponent, ModeloDialogData, ModeloRequest>(ModeloDialogComponent, {
        ...TAMANIO_DIALOGO,
        data: { modelo: null, marcas: this.marcas() },
      })
      .afterClosed()
      .subscribe(datos => {
        if (!datos) return;
        this.ejecutar(this.modeloService.crear(datos), MSG.creado);
      });
  }

  abrirEditar(modelo: Modelo): void {
    this.error.set('');
    this.dialog
      .open<ModeloDialogComponent, ModeloDialogData, ModeloRequest>(ModeloDialogComponent, {
        ...TAMANIO_DIALOGO,
        data: { modelo, marcas: this.marcas() },
      })
      .afterClosed()
      .subscribe(datos => {
        if (!datos) return;
        this.ejecutar(this.modeloService.actualizar(modelo.id, datos), MSG.actualizado);
      });
  }

  abrirBorrar(modelo: Modelo): void {
    this.error.set('');
    this.dialog
      .open<ConfirmarDialogComponent, ConfirmarDialogData, boolean>(ConfirmarDialogComponent, {
        ...TAMANIO_DIALOGO,
        data: {
          titulo: 'Eliminar modelo',
          mensaje: `¿Seguro que querés eliminar el modelo ${modelo.marca} ${modelo.nombre}? Esta acción no se puede deshacer.`,
          accion: 'Eliminar',
        },
      })
      .afterClosed()
      .subscribe(confirmado => {
        if (!confirmado) return;
        this.ejecutar(this.modeloService.eliminar(modelo.id), MSG.eliminado, MSG.errorEliminar);
      });
  }

  protected etiquetaTipo(modelo: Modelo): string {
    return ETIQUETA_TIPO_MODELO[modelo.tipoModelo];
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
