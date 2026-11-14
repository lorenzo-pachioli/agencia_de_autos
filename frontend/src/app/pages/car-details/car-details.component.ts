import { HttpErrorResponse } from '@angular/common/http';
import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { MatAnchor } from '@angular/material/button';
import { MatCard, MatCardContent } from '@angular/material/card';
import { MatDivider } from '@angular/material/divider';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import VehicleResponse from '../../models/vehicle-response-model';
import { ClientServiceService } from '../../services/client-service.service';

@Component({
  imports: [RouterLink, MatAnchor, MatCard, MatCardContent, MatDivider, MatProgressSpinner],
  selector: 'app-car-details',
  styleUrl: './car-details.component.scss',
  templateUrl: './car-details.component.html',
})
export class CarDetailsComponent implements OnInit {
  private clientService = inject(ClientServiceService)
  private activatedRoute = inject(ActivatedRoute)
  private router = inject(Router)

  // Signals: la app no usa zone.js, así que los datos que llegan del backend tienen que ser signals
  vehiculoSeleccionado = signal<VehicleResponse | null>(null)
  estaCargando = signal(true)
  mensajeDeError = signal<string | null>(null)
  urlDeImagenMostrada = signal<string | null>(null)

  // Imagen principal + el resto, sin repetir y sin vacíos
  listaDeImagenes = computed(() => {
    const vehiculo = this.vehiculoSeleccionado()
    if (!vehiculo) return []
    const todasLasUrls = [vehiculo.imagenPrincipalUrl, ...(vehiculo.imagenes ?? [])]
    return [...new Set(todasLasUrls.filter((url): url is string => !!url))]
  })

  private etiquetasDeEstado: Record<VehicleResponse['estado'], string> = {
    DISPONIBLE: 'Disponible',
    RESERVADO: 'Reservado',
    VENDIDO: 'Vendido',
    BAJA: 'Dado de baja',
  }
  private etiquetasDeCombustible: Record<VehicleResponse['tipoCombustible'], string> = {
    NAFTA: 'Nafta',
    DIESEL: 'Diesel',
    HIBRIDO: 'Híbrido',
    ELECTRICO: 'Eléctrico',
  }
  private etiquetasDeTransmision: Record<VehicleResponse['tipoTransmision'], string> = {
    MANUAL: 'Manual',
    AUTOMATICA: 'Automática',
  }

  private formateadorDePrecio = new Intl.NumberFormat('es-AR', {
    style: 'currency',
    currency: 'ARS',
    maximumFractionDigits: 0,
  })
  private formateadorDeNumero = new Intl.NumberFormat('es-AR')
  private formateadorDeFecha = new Intl.DateTimeFormat('es-AR', { dateStyle: 'long' })

  ngOnInit() {
    this.traerVehiculoSeleccionado()
  }

  traerVehiculoSeleccionado() {
    const identificadorEnLaRuta = this.activatedRoute.snapshot.paramMap.get('id')
    const identificadorDelVehiculo = Number(identificadorEnLaRuta)

    if (identificadorEnLaRuta === null || Number.isNaN(identificadorDelVehiculo)) {
      this.estaCargando.set(false)
      this.mensajeDeError.set('El identificador del vehículo en la dirección no es válido.')
      return
    }

    this.clientService.getVehicleById(identificadorDelVehiculo).subscribe({
      next: (vehiculoRecibido) => {
        this.vehiculoSeleccionado.set(vehiculoRecibido)
        this.estaCargando.set(false)
        // La imagen grande arranca con la principal (o la primera que haya)
        this.urlDeImagenMostrada.set(this.listaDeImagenes()[0] ?? null)
      },
      error: (errorDelServidor: HttpErrorResponse) => {
        this.estaCargando.set(false)
        this.mensajeDeError.set(this.armarMensajeDeError(errorDelServidor))
      },
    })
  }

  mostrarImagen(urlDeLaImagen: string) {
    this.urlDeImagenMostrada.set(urlDeLaImagen)
  }

  borrarAuto() {
    const vehiculo = this.vehiculoSeleccionado()
    if (!vehiculo) return

    this.clientService.deleteVehicle(vehiculo.id).subscribe({
      next: () => {
        alert('Vehículo eliminado con éxito')
        this.router.navigate(['/list-client'])
      },
      error: (errorDelServidor: HttpErrorResponse) =>
        alert(this.armarMensajeDeError(errorDelServidor)),
    })
  }

  // ── Formateo para mostrar en pantalla ─────────────────────────────────────
  formatearPrecio(precio: number): string {
    return this.formateadorDePrecio.format(precio)
  }

  formatearKilometraje(kilometraje: number): string {
    return `${this.formateadorDeNumero.format(kilometraje)} km`
  }

  formatearFecha(fechaEnTexto: string): string {
    return this.formateadorDeFecha.format(new Date(fechaEnTexto))
  }

  obtenerEtiquetaDeEstado(estado: VehicleResponse['estado']): string {
    return this.etiquetasDeEstado[estado]
  }

  obtenerEtiquetaDeCombustible(combustible: VehicleResponse['tipoCombustible']): string {
    return this.etiquetasDeCombustible[combustible]
  }

  obtenerEtiquetaDeTransmision(transmision: VehicleResponse['tipoTransmision']): string {
    return this.etiquetasDeTransmision[transmision]
  }

  private armarMensajeDeError(errorDelServidor: HttpErrorResponse): string {
    if (errorDelServidor.status === 0) return 'No se pudo conectar con el servidor.'
    if (errorDelServidor.status === 404) return 'El vehículo no existe o fue eliminado.'
    if (errorDelServidor.status === 401 || errorDelServidor.status === 403) {
      return 'No tenés permisos para realizar esta acción.'
    }
    return errorDelServidor.error?.message ?? 'Ocurrió un error inesperado.'
  }
}
