import { CommonModule } from '@angular/common';
import { Component, TemplateRef, inject, signal, viewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { Observable } from 'rxjs';
import { TransaccionService } from '../../../services/transaccion.service';
import { Pagina } from '../../../models/Vehiculo';
import { MatTableModule } from '@angular/material/table';
import { MatDialog, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MAT_DATE_LOCALE, provideNativeDateAdapter } from '@angular/material/core';
import {
  CrearTransaccion,
  ETIQUETA_ESTADO,
  ETIQUETA_METODO_PAGO,
  EstadoTransaccion,
  FiltrosTransaccion,
  MetodoPago,
  Transaccion,
} from '../../../models/Transacciones';
import { MatButton } from '@angular/material/button';

type Modal = 'crear' | 'senia' | 'venta' | 'cancelar';

const DURACION_MENSAJE_MS = 3500;

const MSG = {
  camposObligatorios: 'Completá precio, vehículo, cliente y vendedor.',
  montoInvalido: 'Ingresá un monto mayor a cero.',
  errorCarga: 'No se pudieron cargar las transacciones.',
  errorOperacion: 'No se pudo completar la operación. Revisá los datos e intentá nuevamente.',
  creada: 'Transacción creada.',
  senia: 'Seña registrada.',
  venta: 'Venta registrada.',
  cancelada: 'Transacción cancelada.',
};

@Component({
  selector: 'app-transacciones',
  imports: [
    CommonModule,
    FormsModule,
    MatPaginatorModule,
    MatTableModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatDatepickerModule,
    MatButton
  ],
  providers: [{ provide: MAT_DATE_LOCALE, useValue: 'es-AR' }, provideNativeDateAdapter()],
  templateUrl: './transacciones.component.html',
  styleUrl: './transacciones.component.scss',
})
export class TransaccionesComponent {
  private readonly transaccionService = inject(TransaccionService);
  private readonly dialog = inject(MatDialog);
  private readonly plantillaDialogo = viewChild.required<TemplateRef<unknown>>('dialogo');
  private dialogRef: MatDialogRef<unknown> | null = null;

  protected readonly EstadoTransaccion = EstadoTransaccion;
  protected readonly etiquetaEstado = ETIQUETA_ESTADO;
  protected readonly etiquetaMetodoPago = ETIQUETA_METODO_PAGO;
  protected readonly estados = signal<EstadoTransaccion[]>(Object.values(EstadoTransaccion));
  protected readonly metodosPago = signal<MetodoPago[]>(Object.values(MetodoPago));
  protected readonly columnas = ['id', 'vehiculo', 'cliente', 'vendedor', 'estado', 'precio', 'acciones'];

  readonly transacciones = signal<Pagina<Transaccion> | null>(null);
  readonly cargando = signal(false);
  readonly error = signal('');
  readonly mensaje = signal('');
  readonly estadoActivo = signal<EstadoTransaccion | null>(null);

  readonly pageSize = 10;
  pageIndex = 0;
  fechaDesde: Date | null = null;
  fechaHasta: Date | null = null;
  modal: Modal | null = null;
  transaccionSeleccionada: Transaccion | null = null;
  formulario: CrearTransaccion = this.formularioVacio();
  montoAccion: number | null = null;

  constructor() {
    this.cargar();
  }

  cargar(): void {
    this.cargando.set(true);
    this.error.set('');

    const filtros: FiltrosTransaccion = {
      estadoTransaccion: this.estadoActivo() ?? undefined,
      fechaDesde: this.fechaDesde ? this.transaccionService.formatearFecha(this.fechaDesde) : undefined,
      fechaHasta: this.fechaHasta ? this.transaccionService.formatearFecha(this.fechaHasta) : undefined,
    };

    this.transaccionService.listar(this.pageIndex, this.pageSize, filtros).subscribe({
      next: pagina => {
        this.transacciones.set(pagina);
        this.cargando.set(false);
      },
      error: () => {
        this.error.set(MSG.errorCarga);
        this.cargando.set(false);
      },
    });
  }

  filtrarEstado(estado: EstadoTransaccion | null): void {
    this.estadoActivo.set(estado);
    this.recargarDesdeInicio();
  }

  aplicarFechas(): void {
    this.recargarDesdeInicio();
  }

  limpiarFechas(): void {
    this.fechaDesde = null;
    this.fechaHasta = null;
    this.recargarDesdeInicio();
  }

  cambiarPagina(evento: PageEvent): void {
    this.pageIndex = evento.pageIndex;
    this.cargar();
  }

  abrirCrear(): void {
    this.formulario = this.formularioVacio();
    this.abrirModal('crear');
  }

  abrirAccion(tipo: 'senia' | 'venta', transaccion: Transaccion): void {
    this.transaccionSeleccionada = transaccion;
    this.montoAccion = transaccion.precio_final;
    this.abrirModal(tipo);
  }

  crear(): void {
    const { precio_final, vehiculo_id, cliente_id, vendedor_id } = this.formulario;
    if (!precio_final || !vehiculo_id || !cliente_id || !vendedor_id) {
      this.error.set(MSG.camposObligatorios);
      return;
    }
    this.ejecutar(this.transaccionService.crear(this.formulario), MSG.creada);
  }

  confirmarAccion(): void {
    const transaccion = this.transaccionSeleccionada;
    const monto = this.montoAccion;
    if (!transaccion || !monto || monto <= 0) {
      this.error.set(MSG.montoInvalido);
      return;
    }

    const esSenia = this.modal === 'senia';
    const solicitud = esSenia
      ? this.transaccionService.senar(transaccion.id, monto)
      : this.transaccionService.vender(transaccion.id, monto);

    this.ejecutar(solicitud, esSenia ? MSG.senia : MSG.venta);
  }

  cancelar(transaccion: Transaccion): void {
    this.transaccionSeleccionada = transaccion;
    this.abrirModal('cancelar');
  }

  confirmarCancelacion(): void {
    const transaccion = this.transaccionSeleccionada;
    if (!transaccion) return;
    this.ejecutar(this.transaccionService.cancelar(transaccion.id), MSG.cancelada);
  }

  cerrarModal(): void {
    this.dialogRef?.close();
  }

  formatearPrecio(precio: number): string {
    return new Intl.NumberFormat('es-AR', { maximumFractionDigits: 0 }).format(precio);
  }

  private abrirModal(modal: Modal): void {
    this.error.set('');
    this.modal = modal;
    this.dialogRef = this.dialog.open(this.plantillaDialogo(), { width: '480px', maxWidth: '92vw' });
    this.dialogRef.afterClosed().subscribe(() => {
      this.modal = null;
      this.dialogRef = null;
      this.error.set('');
    });
  }

  private recargarDesdeInicio(): void {
    this.pageIndex = 0;
    this.cargar();
  }

  private ejecutar(solicitud: Observable<Transaccion>, exito: string): void {
    this.error.set('');
    solicitud.subscribe({
      next: () => {
        this.cerrarModal();
        this.mostrarMensaje(exito);
        this.cargar();
      },
      error: () => this.error.set(MSG.errorOperacion),
    });
  }

  private mostrarMensaje(texto: string): void {
    this.mensaje.set(texto);
    setTimeout(() => this.mensaje.set(''), DURACION_MENSAJE_MS);
  }

  private formularioVacio(): CrearTransaccion {
    return {
      precio_final: 0,
      metodoPago: MetodoPago.EFECTIVO,
      vehiculo_id: 0,
      cliente_id: 0,
      vendedor_id: 0,
      observaciones: '',
    };
  }

  protected etiquetaDe(estado: EstadoTransaccion): string {
    return ETIQUETA_ESTADO[estado];
  }
}