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
  AsignarVisita,
  CrearTransaccion,
  ETIQUETA_ESTADO,
  ETIQUETA_METODO_PAGO,
  ETIQUETA_SOLICITUD,
  EstadoTransaccion,
  FiltrosTransaccion,
  MetodoPago,
  SIN_OBSERVACIONES,
  Transaccion,
} from '../../../models/Transacciones';
import { MatButton } from '@angular/material/button';
import { AuthService } from '../../../services/auth.service';
import { UsuarioService } from '../../../services/usuario.service';
import {
  AccionMontoDialogComponent,
  AccionMontoDialogData,
} from '../../../components/accion-monto-dialog/accion-monto-dialog.component';
import {
  RegistrarReservaDialogComponent,
  DatosReserva,
} from '../../../components/registrar-reserva-dialog/registrar-reserva-dialog.component';
import { AgendarVisitaDialogComponent } from '../../../components/agendar-visita-dialog/agendar-visita-dialog.component';

type Modal = 'crear' | 'cancelar';

const DURACION_MENSAJE_MS = 3500;

const TAMANIO_DIALOGO = { width: '480px', maxWidth: '92vw' };

const ASUNTO_CONTACTO = 'Visita a la agencia por el vehículo';

const MSG = {
  camposObligatorios: 'Completá precio, vehículo, cliente y vendedor.',
  errorCarga: 'No se pudieron cargar las transacciones.',
  errorOperacion: 'No se pudo completar la operación. Revisá los datos e intentá nuevamente.',
  creada: 'Transacción creada.',
  senia: 'Seña registrada.',
  venta: 'Venta registrada.',
  asignada: 'Solicitud asignada a tu nombre.',
  visitaAgendada: 'Visita agendada.',
  reserva: 'Reserva registrada.',
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
  private readonly auth = inject(AuthService);
  private readonly usuarioService = inject(UsuarioService);
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
  readonly vendedorId = signal<number | null>(null);

  readonly pageSize = 10;
  pageIndex = 0;
  fechaDesde: Date | null = null;
  fechaHasta: Date | null = null;
  modal: Modal | null = null;
  transaccionSeleccionada: Transaccion | null = null;
  formulario: CrearTransaccion = this.formularioVacio();

  constructor() {
    this.cargar();
    if (this.auth.isVendedor()) {
      this.usuarioService.getMe().subscribe(usuario => this.vendedorId.set(usuario.id));
    }
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
    this.error.set('');
    this.dialog
      .open<AccionMontoDialogComponent, AccionMontoDialogData, number>(AccionMontoDialogComponent, {
        ...TAMANIO_DIALOGO,
        data: { tipo, transaccion },
      })
      .afterClosed()
      .subscribe(monto => {
        if (!monto) return;
        const solicitud = tipo === 'senia'
          ? this.transaccionService.senar(transaccion.id, monto)
          : this.transaccionService.vender(transaccion.id, monto);
        this.ejecutar(solicitud, tipo === 'senia' ? MSG.senia : MSG.venta);
      });
  }

  asignarme(transaccion: Transaccion): void {
    this.ejecutar(this.transaccionService.asignar(transaccion.id, { fechaVisita: null }), MSG.asignada);
  }

  abrirAgendarVisita(transaccion: Transaccion): void {
    this.error.set('');
    this.dialog
      .open<AgendarVisitaDialogComponent, Transaccion, AsignarVisita>(AgendarVisitaDialogComponent, {
        ...TAMANIO_DIALOGO,
        data: transaccion,
      })
      .afterClosed()
      .subscribe(datos => {
        if (!datos) return;
        this.ejecutar(this.transaccionService.asignar(transaccion.id, datos), MSG.visitaAgendada);
      });
  }

  abrirRegistrarReserva(transaccion: Transaccion): void {
    const vendedorId = this.vendedorId();
    if (!vendedorId) return;

    this.error.set('');
    this.dialog
      .open<RegistrarReservaDialogComponent, Transaccion, DatosReserva>(RegistrarReservaDialogComponent, {
        ...TAMANIO_DIALOGO,
        data: transaccion,
      })
      .afterClosed()
      .subscribe(datos => {
        if (!datos) return;
        const solicitud = this.transaccionService.actualizar(transaccion.id, {
          ...datos,
          observaciones: transaccion.observaciones || SIN_OBSERVACIONES,
          vehiculo_id: transaccion.vehiculo_id,
          cliente_id: transaccion.cliente_id,
          vendedor_id: vendedorId,
        });
        this.ejecutar(solicitud, MSG.reserva);
      });
  }

  crear(): void {
    const { precio_final, vehiculo_id, cliente_id, vendedor_id } = this.formulario;
    if (!precio_final || !vehiculo_id || !cliente_id || !vendedor_id) {
      this.error.set(MSG.camposObligatorios);
      return;
    }
    this.ejecutar(this.transaccionService.crear(this.formulario), MSG.creada);
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
    this.dialogRef = this.dialog.open(this.plantillaDialogo(), TAMANIO_DIALOGO);
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

  protected etiquetaDe(transaccion: Transaccion): string {
    if (transaccion.estadoTransaccion !== EstadoTransaccion.SOLICITAR) return ETIQUETA_ESTADO[transaccion.estadoTransaccion];
    if (transaccion.fecha_visita) return `${ETIQUETA_SOLICITUD.visita} ${this.formatearFechaVisita(transaccion.fecha_visita)}`;
    return transaccion.vendedor_id ? ETIQUETA_SOLICITUD.asignada : ETIQUETA_SOLICITUD.sinAsignar;
  }

  protected estaLibre(transaccion: Transaccion): boolean {
    return transaccion.estadoTransaccion === EstadoTransaccion.SOLICITAR
      && transaccion.vendedor_id === null
      && this.vendedorId() !== null;
  }

  protected esMia(transaccion: Transaccion): boolean {
    return transaccion.estadoTransaccion === EstadoTransaccion.SOLICITAR
      && transaccion.vendedor_id !== null
      && transaccion.vendedor_id === this.vendedorId();
  }

  protected contactoCliente(transaccion: Transaccion): string {
    const asunto = encodeURIComponent(`${ASUNTO_CONTACTO} ${transaccion.patente}`);
    return `mailto:${transaccion.cliente_email}?subject=${asunto}`;
  }

  private formatearFechaVisita(fecha: string): string {
    return new Intl.DateTimeFormat('es-AR', { day: '2-digit', month: '2-digit', hour: '2-digit', minute: '2-digit' })
      .format(new Date(fecha));
  }
}