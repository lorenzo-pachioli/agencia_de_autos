import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, TemplateRef, inject, signal, viewChild } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FormsModule } from '@angular/forms';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { Observable, Subject, catchError, debounceTime, of, switchMap } from 'rxjs';
import { TransaccionService } from '../../../services/transaccion.service';
import { EstadoVehiculo, Pagina, Vehiculo } from '../../../models/Vehiculo';
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
  TipoOperacion,
  Transaccion,
} from '../../../models/Transacciones';
import { MatButton } from '@angular/material/button';
import { AuthService } from '../../../services/auth.service';
import { UsuarioService } from '../../../services/usuario.service';
import { VehiculoService } from '../../../services/vehiculo-service.service';
import { ConfiguracionService } from '../../../services/configuracion.service';
import { Configuracion, RANGO_PORCENTAJE_SENIA } from '../../../models/Configuracion';
import { datosOperacion, errorMontoReserva, formatearMonto } from '../../../models/Operacion';
import type { ApiError } from '../../../models/ApiError';
import {
  RegistrarReservaDialogComponent,
  RegistrarReservaData,
  DatosReserva,
} from '../../../components/registrar-reserva-dialog/registrar-reserva-dialog.component';
import { AgendarVisitaDialogComponent } from '../../../components/agendar-visita-dialog/agendar-visita-dialog.component';
import { CamposOperacionComponent } from '../../../components/campos-operacion/campos-operacion.component';

type Modal = 'crear' | 'cancelar' | 'vender' | 'senia';

const DURACION_MENSAJE_MS = 3500;
const ESPERA_BUSQUEDA_VEHICULO_MS = 300;

const TAMANIO_DIALOGO = { width: '480px', maxWidth: '92vw' };

const ASUNTO_CONTACTO = 'Visita a la agencia por el vehículo';

const MSG = {
  camposObligatorios: 'Completá vehículo, cliente y vendedor.',
  vehiculoInvalido: 'Ingresá el ID de un vehículo disponible.',
  porcentajeInvalido: `Ingresá un porcentaje entre ${RANGO_PORCENTAJE_SENIA.minimo} y ${RANGO_PORCENTAJE_SENIA.maximo}.`,
  errorCarga: 'No se pudieron cargar las transacciones.',
  errorConfiguracion: 'No se pudo consultar el porcentaje de seña. Intentá nuevamente.',
  errorOperacion: 'No se pudo completar la operación. Revisá los datos e intentá nuevamente.',
  venta: 'Venta registrada.',
  asignada: 'Solicitud asignada a tu nombre.',
  visitaAgendada: 'Visita agendada.',
  cancelada: 'Transacción cancelada.',
  porcentajeActualizado: 'Porcentaje de seña actualizado.',
};

const MENSAJE_REGISTRO: Record<TipoOperacion, string> = {
  [EstadoTransaccion.RESERVA]: 'Reserva registrada.',
  [EstadoTransaccion.SENIADO]: 'Seña registrada.',
  [EstadoTransaccion.VENDIDO]: MSG.venta,
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
    MatButton,
    CamposOperacionComponent,
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
  private readonly vehiculoService = inject(VehiculoService);
  private readonly configuracionService = inject(ConfiguracionService);
  private readonly plantillaDialogo = viewChild.required<TemplateRef<unknown>>('dialogo');
  private readonly vehiculoBuscado = new Subject<number>();
  private dialogRef: MatDialogRef<unknown> | null = null;

  protected readonly EstadoTransaccion = EstadoTransaccion;
  protected readonly EstadoVehiculo = EstadoVehiculo;
  protected readonly etiquetaEstado = ETIQUETA_ESTADO;
  protected readonly etiquetaMetodoPago = ETIQUETA_METODO_PAGO;
  protected readonly rangoSenia = RANGO_PORCENTAJE_SENIA;
  protected readonly esAdmin = this.auth.isAdmin;
  protected readonly estados = signal<EstadoTransaccion[]>(Object.values(EstadoTransaccion));
  protected readonly metodosPago = signal<MetodoPago[]>(Object.values(MetodoPago));
  protected readonly columnas = [
    'id', 'vehiculo', 'cliente', 'vendedor', 'estado', 'precio', 'entregado', 'saldo', 'acciones',
  ];

  readonly transacciones = signal<Pagina<Transaccion> | null>(null);
  readonly cargando = signal(false);
  readonly error = signal('');
  readonly mensaje = signal('');
  readonly estadoActivo = signal<EstadoTransaccion | null>(null);
  readonly vendedorId = signal<number | null>(null);
  readonly configuracion = signal<Configuracion | null>(null);
  readonly vehiculo = signal<Vehiculo | null>(null);
  readonly vehiculoNoEncontrado = signal(false);

  readonly pageSize = 10;
  pageIndex = 0;
  fechaDesde: Date | null = null;
  fechaHasta: Date | null = null;
  modal: Modal | null = null;
  transaccionSeleccionada: Transaccion | null = null;
  formulario: CrearTransaccion = this.formularioVacio();
  porcentajeSenia: number | null = null;

  constructor() {
    this.cargar();
    if (this.auth.isVendedor()) {
      this.usuarioService.getMe().subscribe(usuario => this.vendedorId.set(usuario.id));
    }
    if (this.auth.isAdmin()) {
      this.conConfiguracion(() => {});
    }
    this.escucharBusquedaVehiculo();
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
    this.conConfiguracion(() => {
      this.formulario = this.formularioVacio();
      this.buscarVehiculo(null);
      this.abrirModal('crear');
    });
  }

  buscarVehiculo(id: number | null): void {
    this.vehiculo.set(null);
    this.vehiculoNoEncontrado.set(false);
    this.vehiculoBuscado.next(id ?? 0);
  }

  abrirPorcentajeSenia(): void {
    this.conConfiguracion(configuracion => {
      this.porcentajeSenia = configuracion.porcentaje_senia;
      this.abrirModal('senia');
    });
  }

  guardarPorcentajeSenia(): void {
    const porcentaje = this.porcentajeSenia;
    if (porcentaje === null || porcentaje < RANGO_PORCENTAJE_SENIA.minimo || porcentaje > RANGO_PORCENTAJE_SENIA.maximo) {
      this.error.set(MSG.porcentajeInvalido);
      return;
    }
    this.ejecutar(
      this.configuracionService.actualizar(porcentaje),
      MSG.porcentajeActualizado,
      configuracion => this.configuracion.set(configuracion),
    );
  }

  abrirVenta(transaccion: Transaccion): void {
    this.transaccionSeleccionada = transaccion;
    this.abrirModal('vender');
  }

  confirmarVenta(): void {
    const transaccion = this.transaccionSeleccionada;
    if (!transaccion) return;
    this.ejecutar(this.transaccionService.vender(transaccion.id), MSG.venta);
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

    this.conConfiguracion(configuracion => {
      this.dialog
        .open<RegistrarReservaDialogComponent, RegistrarReservaData, DatosReserva>(RegistrarReservaDialogComponent, {
          ...TAMANIO_DIALOGO,
          data: { transaccion, configuracion },
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
          this.ejecutar(solicitud, MENSAJE_REGISTRO[datos.estadoTransaccion]);
        });
    });
  }

  crear(): void {
    const { vehiculo_id, cliente_id, vendedor_id, estadoTransaccion, monto_entregado } = this.formulario;
    if (!vehiculo_id || !cliente_id || !vendedor_id) {
      this.error.set(MSG.camposObligatorios);
      return;
    }

    const vehiculo = this.vehiculo();
    const configuracion = this.configuracion();
    if (!configuracion || vehiculo?.id !== vehiculo_id || vehiculo.estado !== EstadoVehiculo.DISPONIBLE) {
      this.error.set(MSG.vehiculoInvalido);
      return;
    }

    const errorMonto = errorMontoReserva(estadoTransaccion, vehiculo.precioVenta, configuracion, monto_entregado);
    if (errorMonto) {
      this.error.set(errorMonto);
      return;
    }

    const solicitud = this.transaccionService.crear({
      ...this.formulario,
      ...datosOperacion(estadoTransaccion, monto_entregado),
    });
    this.ejecutar(solicitud, MENSAJE_REGISTRO[estadoTransaccion]);
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

  protected formatearImporte(monto: number | null | undefined): string {
    return monto == null ? '—' : `$ ${formatearMonto(monto)}`;
  }

  protected saldoDe(transaccion: Transaccion): number | null {
    const { precio_final, monto_entregado, estadoTransaccion } = transaccion;
    if (estadoTransaccion === EstadoTransaccion.CANCELADO) return null;
    if (precio_final == null || monto_entregado == null) return null;
    return precio_final - monto_entregado;
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

  /** Consulta el porcentaje vigente antes de seguir, por si el administrador lo cambió. */
  private conConfiguracion(accion: (configuracion: Configuracion) => void): void {
    this.configuracionService.obtener().subscribe({
      next: configuracion => {
        this.configuracion.set(configuracion);
        accion(configuracion);
      },
      error: () => this.error.set(MSG.errorConfiguracion),
    });
  }

  private escucharBusquedaVehiculo(): void {
    this.vehiculoBuscado
      .pipe(
        debounceTime(ESPERA_BUSQUEDA_VEHICULO_MS),
        switchMap(id => {
          if (id <= 0) return of(null);
          return this.vehiculoService.obtenerPorId(String(id)).pipe(
            catchError(() => {
              this.vehiculoNoEncontrado.set(true);
              return of(null);
            }),
          );
        }),
        takeUntilDestroyed(),
      )
      .subscribe(vehiculo => this.vehiculo.set(vehiculo));
  }

  private recargarDesdeInicio(): void {
    this.pageIndex = 0;
    this.cargar();
  }

  private ejecutar<T>(solicitud: Observable<T>, exito: string, alCompletar?: (resultado: T) => void): void {
    this.error.set('');
    solicitud.subscribe({
      next: resultado => {
        alCompletar?.(resultado);
        this.cerrarModal();
        this.mostrarMensaje(exito);
        this.cargar();
      },
      error: (respuesta: HttpErrorResponse) => this.error.set(this.mensajeDeError(respuesta)),
    });
  }

  private mensajeDeError(respuesta: HttpErrorResponse): string {
    const detalle = respuesta.error as Partial<ApiError> | null;
    const validaciones = Object.values(detalle?.validationErrors ?? {});
    if (validaciones.length) return validaciones.join(' ');
    return detalle?.message || MSG.errorOperacion;
  }

  private mostrarMensaje(texto: string): void {
    this.mensaje.set(texto);
    setTimeout(() => this.mensaje.set(''), DURACION_MENSAJE_MS);
  }

  private formularioVacio(): CrearTransaccion {
    return {
      estadoTransaccion: EstadoTransaccion.RESERVA,
      monto_entregado: null,
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
