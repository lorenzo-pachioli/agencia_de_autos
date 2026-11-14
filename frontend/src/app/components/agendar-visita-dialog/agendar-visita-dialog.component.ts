import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButton } from '@angular/material/button';
import { MAT_DATE_LOCALE, provideNativeDateAdapter } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { AsignarVisita, Transaccion } from '../../models/Transacciones';

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
  protected readonly transaccion = inject<Transaccion>(MAT_DIALOG_DATA);
  private readonly dialogRef = inject<MatDialogRef<AgendarVisitaDialogComponent, AsignarVisita>>(MatDialogRef);

  protected readonly hoy = new Date();
  protected readonly esReprogramacion = this.transaccion.fecha_visita !== null;
  protected readonly error = signal('');
  protected dia: Date | null = this.transaccion.fecha_visita ? new Date(this.transaccion.fecha_visita) : null;
  protected hora = this.transaccion.fecha_visita?.slice(11, 16) ?? '';

  protected confirmar(): void {
    if (!this.dia || !this.hora) {
      this.error.set(MSG.fechaIncompleta);
      return;
    }

    const [horas, minutos] = this.hora.split(':').map(Number);
    const fecha = new Date(this.dia);
    fecha.setHours(horas, minutos, 0, 0);

    if (fecha <= new Date()) {
      this.error.set(MSG.fechaPasada);
      return;
    }
    this.dialogRef.close({ fechaVisita: this.formatearFechaHora(fecha) });
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
