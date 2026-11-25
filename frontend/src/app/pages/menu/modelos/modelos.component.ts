import { Component, inject, signal } from '@angular/core';
import { MatButton } from '@angular/material/button';
import { MatDialog } from '@angular/material/dialog';
import { MatTableModule } from '@angular/material/table';
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
  errorEliminar: 'No se pudo eliminar el modelo.',
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
        data: { modelo: null, marcas: this.marcas(), guardar: datos => this.modeloService.crear(datos) },
      })
      .afterClosed()
      .subscribe(datos => {
        if (datos) this.confirmarExito(MSG.creado);
      });
  }

  abrirEditar(modelo: Modelo): void {
    this.error.set('');
    this.dialog
      .open<ModeloDialogComponent, ModeloDialogData, ModeloRequest>(ModeloDialogComponent, {
        ...TAMANIO_DIALOGO,
        data: { modelo, marcas: this.marcas(), guardar: datos => this.modeloService.actualizar(modelo.id, datos) },
      })
      .afterClosed()
      .subscribe(datos => {
        if (datos) this.confirmarExito(MSG.actualizado);
      });
  }

  abrirBorrar(modelo: Modelo): void {
    this.error.set('');
    this.dialog
      .open<ConfirmarDialogComponent, ConfirmarDialogData, boolean>(ConfirmarDialogComponent, {
        ...TAMANIO_DIALOGO,
        data: {
          titulo: 'Eliminar modelo',
          mensaje: `¿Seguro que querés eliminar el modelo ${modelo.marca} ${modelo.nombre}? Va a dejar de mostrarse en la agencia.`,
          accion: 'Eliminar',
          ejecutar: () => this.modeloService.eliminar(modelo.id),
          errorPorDefecto: MSG.errorEliminar,
        },
      })
      .afterClosed()
      .subscribe(confirmado => {
        if (confirmado) this.confirmarExito(MSG.eliminado);
      });
  }

  protected etiquetaTipo(modelo: Modelo): string {
    return ETIQUETA_TIPO_MODELO[modelo.tipoModelo];
  }

  private confirmarExito(texto: string): void {
    this.mostrarMensaje(texto);
    this.cargar();
  }

  private mostrarMensaje(texto: string): void {
    this.mensaje.set(texto);
    setTimeout(() => this.mensaje.set(''), DURACION_MENSAJE_MS);
  }
}
