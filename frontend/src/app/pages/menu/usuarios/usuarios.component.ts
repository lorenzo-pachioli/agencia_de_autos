import { Component, inject } from '@angular/core';
import { UsuarioService } from '../../../services/usuario.service';
import { toSignal } from '@angular/core/rxjs-interop';
import { catchError } from 'rxjs/internal/operators/catchError';
import { of } from 'rxjs';
import { MatTableModule } from '@angular/material/table';
import { DatePipe } from '@angular/common';

@Component({
  imports: [MatTableModule, DatePipe],
  selector: 'app-usuarios',
  styleUrl: './usuarios.component.scss',
  templateUrl: './usuarios.component.html',
})
export class UsuariosComponent {
  private readonly usuario_service = inject(UsuarioService);

  readonly columnas = ['id', 'nombre', 'apellido', 'email', 'rolUsuario', 'activo', 'alta', 'acciones'];

  readonly usuarios = toSignal(
    this.usuario_service.getAll().pipe(
      catchError(() => of(null))
    ),
    { initialValue: undefined }
  );
}
