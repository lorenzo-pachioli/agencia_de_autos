import { Component, inject, signal } from '@angular/core';
import { MatButton } from '@angular/material/button';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatTableModule } from '@angular/material/table';
import { Observable } from 'rxjs';
import {
  ConfirmarDialogComponent,
  ConfirmarDialogData,
} from '../../../components/confirmar-dialog/confirmar-dialog.component';
import {
  VehiculoDialogComponent,
  VehiculoDialogData,
} from '../../../components/vehiculo-dialog/vehiculo-dialog.component';
import { Marca, Modelo } from '../../../models/Opciones';
import { ETIQUETA_ESTADO_VEHICULO, Pagina, VehiculoGestion, VehiculoRequest } from '../../../models/Vehiculo';
import { AuthService } from '../../../services/auth.service';
import { MarcaService } from '../../../services/marca.service';
import { ModeloService } from '../../../services/modelo.service';
import { VehiculoService } from '../../../services/vehiculo-service.service';

const DURACION_MENSAJE_MS = 3500;

const TAMANIO_DIALOGO = { width: '480px', maxWidth: '92vw' };

const TAMANIO_DIALOGO_VEHICULO = { width: '720px', maxWidth: '92vw' };

const MSG = {
  errorCarga: 'No se pudieron cargar los vehículos.',
  errorCargaOpciones: 'No se pudieron cargar las marcas y modelos.',
  errorOperacion: 'No se pudo completar la operación. Revisá los datos e intentá nuevamente.',
  errorEliminar: 'No se pudo eliminar el vehículo. Verificá que no tenga transacciones o imágenes asociadas.',
  creado: 'Vehículo cargado.',
  actualizado: 'Vehículo actualizado.',
  eliminado: 'Vehículo eliminado.',
};

@Component({
  imports: [MatTableModule, MatPaginatorModule, MatButton],
  selector: 'app-vehiculos',
  styleUrl: './vehiculos.component.scss',
  templateUrl: './vehiculos.component.html',
})
export class VehiculosComponent {
  private readonly vehiculoService = inject(VehiculoService);
  private readonly marcaService = inject(MarcaService);
  private readonly modeloService = inject(ModeloService);
  private readonly dialog = inject(MatDialog);
  private readonly auth = inject(AuthService);

  protected readonly isAdmin = this.auth.isAdmin;
  protected readonly columnas = ['id', 'patente', 'vehiculo', 'anio', 'precioVenta', 'estado', 'acciones'];

  readonly vehiculos = signal<Pagina<VehiculoGestion> | null>(null);
  readonly marcas = signal<Marca[]>([]);
  readonly modelos = signal<Modelo[]>([]);
  readonly cargando = signal(false);
  readonly error = signal('');
  readonly mensaje = signal('');

  readonly pageSize = 10;
  pageIndex = 0;

  constructor() {
    this.cargar();
    this.marcaService.listar().subscribe({
      next: marcas => this.marcas.set(marcas),
      error: () => this.error.set(MSG.errorCargaOpciones),
    });
    this.modeloService.listar().subscribe({
      next: modelos => this.modelos.set(modelos),
      error: () => this.error.set(MSG.errorCargaOpciones),
    });
  }

  cargar(): void {
    this.cargando.set(true);
    this.vehiculoService.listarGestion(this.pageIndex, this.pageSize).subscribe({
      next: pagina => {
        this.vehiculos.set(pagina);
        this.cargando.set(false);
      },
      error: () => {
        this.error.set(MSG.errorCarga);
        this.cargando.set(false);
      },
    });
  }

  cambiarPagina(evento: PageEvent): void {
    this.pageIndex = evento.pageIndex;
    this.cargar();
  }

  abrirCrear(): void {
    this.error.set('');
    this.dialog
      .open<VehiculoDialogComponent, VehiculoDialogData, VehiculoRequest>(VehiculoDialogComponent, {
        ...TAMANIO_DIALOGO_VEHICULO,
        data: { vehiculo: null, marcas: this.marcas(), modelos: this.modelos() },
      })
      .afterClosed()
      .subscribe(datos => {
        if (!datos) return;
        this.ejecutar(this.vehiculoService.crear(datos), MSG.creado);
      });
  }

  abrirEditar(vehiculo: VehiculoGestion): void {
    this.error.set('');
    this.dialog
      .open<VehiculoDialogComponent, VehiculoDialogData, VehiculoRequest>(VehiculoDialogComponent, {
        ...TAMANIO_DIALOGO_VEHICULO,
        data: { vehiculo, marcas: this.marcas(), modelos: this.modelos() },
      })
      .afterClosed()
      .subscribe(datos => {
        if (!datos) return;
        this.ejecutar(this.vehiculoService.actualizar(vehiculo.id, datos), MSG.actualizado);
      });
  }

  abrirBorrar(vehiculo: VehiculoGestion): void {
    this.error.set('');
    this.dialog
      .open<ConfirmarDialogComponent, ConfirmarDialogData, boolean>(ConfirmarDialogComponent, {
        ...TAMANIO_DIALOGO,
        data: {
          titulo: 'Eliminar vehículo',
          mensaje: `¿Seguro que querés eliminar el vehículo ${vehiculo.patente}? Esta acción no se puede deshacer.`,
          accion: 'Eliminar',
        },
      })
      .afterClosed()
      .subscribe(confirmado => {
        if (!confirmado) return;
        this.ejecutar(this.vehiculoService.eliminar(vehiculo.id), MSG.eliminado, MSG.errorEliminar);
      });
  }

  protected etiquetaEstado(vehiculo: VehiculoGestion): string {
    return ETIQUETA_ESTADO_VEHICULO[vehiculo.estado];
  }

  protected formatearPrecio(precio: number): string {
    return new Intl.NumberFormat('es-AR', { maximumFractionDigits: 0 }).format(precio);
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
