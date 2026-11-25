import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButton } from '@angular/material/button';
import { MAT_DATE_LOCALE, provideNativeDateAdapter } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Observable } from 'rxjs';
import { AsignarVisita, Transaccion } from '../../models/Transacciones';
import { EnvioDeModal } from '../../utils/modales';

export interface AgendarVisitaData {
  transaccion: Transaccion;
  guardar: (datos: AsignarVisita) => Observable<unknown>;
}

const MSG = {
  fechaIncompleta: 'Elegí el día y la hora de la visita.',
  fechaPasada: 'La visita tiene que ser en una fecha y hora futura.',
};

@Component({
  selector: 'app-agendar-visita-dialog',
  imports: [FormsModule, MatDialogModule, MatFormFieldModule, MatInputModule, MatDatepickerModule, MatButton],
  providers: [{ provide: MAT_DATE_LOCALE, useValue: 'es-AR' }, provideNativeDateAdapter()],
  templateUrl: './agendar-visita-dialog.component.html',
})
export class AgendarVisitaDialogComponent {
  private readonly data = inject<AgendarVisitaData>(MAT_DIALOG_DATA);
  private readonly dialogRef = inject<MatDialogRef<AgendarVisitaDialogComponent, AsignarVisita>>(MatDialogRef);

  protected readonly envio = new EnvioDeModal<AsignarVisita>(this.dialogRef);
  protected readonly transaccion = this.data.transaccion;
  protected readonly hoy = new Date();
  protected readonly esReprogramacion = this.transaccion.fecha_visita !== null;
  protected dia: Date | null = this.transaccion.fecha_visita ? new Date(this.transaccion.fecha_visita) : null;
  protected hora = this.transaccion.fecha_visita?.slice(11, 16) ?? '';

  protected confirmar(): void {
    if (!this.dia || !this.hora) {
      this.envio.error.set(MSG.fechaIncompleta);
      return;
    }

    const [horas, minutos] = this.hora.split(':').map(Number);
    const fecha = new Date(this.dia);
    fecha.setHours(horas, minutos, 0, 0);

    if (fecha <= new Date()) {
      this.envio.error.set(MSG.fechaPasada);
      return;
    }
    const datos: AsignarVisita = { fechaVisita: this.formatearFechaHora(fecha) };
    this.envio.enviar(this.data.guardar(datos), datos);
  }

  protected cerrar(): void {
    this.dialogRef.close();
  }

  private formatearFechaHora(fecha: Date): string {
    const anio = fecha.getFullYear();
    const mes = String(fecha.getMonth() + 1).padStart(2, '0');
    const dia = String(fecha.getDate()).padStart(2, '0');
    return `${anio}-${mes}-${dia}T${this.hora}`;
  }
}
