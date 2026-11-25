import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButton } from '@angular/material/button';
import { ErrorStateMatcher } from '@angular/material/core';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { Observable, map } from 'rxjs';
import { Configuracion } from '../../models/Configuracion';
import { datosOperacion, errorMontoReserva } from '../../models/Operacion';
import {
  CrearTransaccion,
  ETIQUETA_METODO_PAGO,
  EstadoTransaccion,
  MetodoPago,
  TipoOperacion,
} from '../../models/Transacciones';
import { Usuario } from '../../models/Usuario';
import { EstadoVehiculo, Vehiculo } from '../../models/Vehiculo';
import { UsuarioService } from '../../services/usuario.service';
import { VehiculoService } from '../../services/vehiculo-service.service';
import { ERROR_CAMPOS, EnvioDeModal } from '../../utils/modales';
import { BuscadorComponent } from '../buscador/buscador.component';
import { CamposOperacionComponent } from '../campos-operacion/campos-operacion.component';

export interface NuevaTransaccionData {
  configuracion: Configuracion;
  /** Viene cargado cuando quien opera es un vendedor; el administrador lo elige. */
  vendedor: Usuario | null;
  guardar: (transaccion: CrearTransaccion) => Observable<unknown>;
}

const MAXIMO_VEHICULOS = 20;
const EMAIL_VALIDO = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

const MSG = {
  vehiculo: 'Elegí un vehículo de la lista.',
  cliente: 'Elegí un cliente de la lista o creá uno nuevo.',
  vendedor: 'Elegí un vendedor de la lista.',
  email: 'Ingresá un email válido.',
};

@Component({
  selector: 'app-nueva-transaccion-dialog',
  imports: [
    FormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButton,
    BuscadorComponent,
    CamposOperacionComponent,
  ],
  templateUrl: './nueva-transaccion-dialog.component.html',
  styleUrl: './nueva-transaccion-dialog.component.scss',
})
export class NuevaTransaccionDialogComponent {
  protected readonly data = inject<NuevaTransaccionData>(MAT_DIALOG_DATA);
  private readonly dialogRef = inject<MatDialogRef<NuevaTransaccionDialogComponent, CrearTransaccion>>(MatDialogRef);
  private readonly vehiculoService = inject(VehiculoService);
  private readonly usuarioService = inject(UsuarioService);

  protected readonly envio = new EnvioDeModal<CrearTransaccion>(this.dialogRef);
  protected readonly metodosPago = Object.values(MetodoPago);
  protected readonly etiquetaMetodoPago = ETIQUETA_METODO_PAGO;

  protected readonly vehiculo = signal<Vehiculo | null>(null);
  protected readonly cliente = signal<Usuario | null>(null);
  protected readonly vendedor = signal<Usuario | null>(this.data.vendedor);
  protected readonly clienteNuevo = signal(false);
  protected readonly emailCliente = signal('');
  protected readonly tipo = signal<TipoOperacion>(EstadoTransaccion.RESERVA);
  protected readonly montoReserva = signal<number | null>(null);
  protected readonly intentoEnviar = signal(false);
  protected metodoPago = MetodoPago.EFECTIVO;
  protected observaciones = '';
  private textoCliente = '';

  protected readonly errorVehiculo = computed(() => (this.intentoEnviar() && !this.vehiculo() ? MSG.vehiculo : ''));
  protected readonly errorVendedor = computed(() => (this.intentoEnviar() && !this.vendedor() ? MSG.vendedor : ''));
  protected readonly errorCliente = computed(() =>
    this.intentoEnviar() && !this.clienteNuevo() && !this.cliente() ? MSG.cliente : '',
  );
  protected readonly errorEmail = computed(() =>
    this.intentoEnviar() && !EMAIL_VALIDO.test(this.emailCliente().trim()) ? MSG.email : '',
  );
  protected readonly estadoEmail: ErrorStateMatcher = { isErrorState: () => !!this.errorEmail() };

  protected readonly buscarVehiculos = (texto: string): Observable<Vehiculo[]> =>
    this.vehiculoService
      .obtenerTodos(0, MAXIMO_VEHICULOS, { texto, estado: EstadoVehiculo.DISPONIBLE })
      .pipe(map(pagina => pagina.content));

  protected readonly buscarClientes = (texto: string): Observable<Usuario[]> =>
    this.usuarioService.buscarClientes(texto);

  protected readonly buscarVendedores = (texto: string): Observable<Usuario[]> =>
    this.usuarioService.getVendedores().pipe(
      map(vendedores =>
        vendedores.filter(v => v.activo && this.describirUsuario(v).toLowerCase().includes(texto.toLowerCase())),
      ),
    );

  protected readonly describirVehiculo = (v: Vehiculo): string => `${v.marcaNombre} ${v.modeloNombre} · ${v.patente}`;

  protected readonly describirUsuario = (u: Usuario): string =>
    u.registrado ? `${u.nombre} ${u.apellido} · ${u.email}` : `${u.email} (sin registrar)`;

  protected elegirCliente(cliente: Usuario | null): void {
    this.cliente.set(cliente);
    if (cliente) this.clienteNuevo.set(false);
  }

  protected recordarTextoCliente(texto: string): void {
    this.textoCliente = texto.trim();
  }

  protected crearCliente(): void {
    // Si ya había escrito un email en el buscador, se reutiliza
    if (this.textoCliente.includes('@')) this.emailCliente.set(this.textoCliente);
    this.clienteNuevo.set(true);
  }

  protected confirmar(): void {
    this.intentoEnviar.set(true);
    const vehiculo = this.vehiculo();
    const vendedor = this.vendedor();
    const cliente = this.datosCliente();

    if (!vehiculo || !vendedor || !cliente
      || errorMontoReserva(this.tipo(), vehiculo.precioVenta, this.data.configuracion, this.montoReserva())) {
      this.envio.error.set(ERROR_CAMPOS);
      return;
    }

    const transaccion: CrearTransaccion = {
      ...cliente,
      ...datosOperacion(this.tipo(), this.montoReserva()),
      vehiculo_id: vehiculo.id,
      vendedor_id: vendedor.id,
      metodoPago: this.metodoPago,
      observaciones: this.observaciones.trim() || undefined,
    };
    this.envio.enviar(this.data.guardar(transaccion), transaccion);
  }

  protected cerrar(): void {
    this.dialogRef.close();
  }

  private datosCliente(): Pick<CrearTransaccion, 'cliente_id' | 'cliente_email'> | null {
    if (this.clienteNuevo()) {
      const email = this.emailCliente().trim();
      return EMAIL_VALIDO.test(email) ? { cliente_email: email } : null;
    }
    const cliente = this.cliente();
    return cliente ? { cliente_id: cliente.id } : null;
  }
}
