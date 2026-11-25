import { Component, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { UsuarioService } from '../../../services/usuario.service';
import { AuthService } from '../../../services/auth.service';
import { ROL, Usuario } from '../../../models/Usuario';

@Component({
  imports: [MatTableModule, MatSelectModule, MatSnackBarModule, DatePipe],
  selector: 'app-usuarios',
  styleUrl: './usuarios.component.scss',
  templateUrl: './usuarios.component.html',
})
export class UsuariosComponent {
  private readonly usuario_service = inject(UsuarioService);
  private readonly snackBar = inject(MatSnackBar);
  readonly auth = inject(AuthService);

  readonly columnas = ['id', 'nombre', 'apellido', 'email', 'rolUsuario', 'activo', 'alta'];
  readonly roles = Object.values(ROL);
  readonly estados = [true, false];

  
  readonly usuarios = signal<Usuario[] | null | undefined>(undefined);
  readonly guardando = signal<ReadonlySet<number>>(new Set());

  constructor() {
    this.usuario_service.getAll().subscribe({
      next: (lista) => this.usuarios.set(lista),
      error: () => this.usuarios.set(null),
    });
  }

  esUsuarioActual(usuario: Usuario): boolean {
    return usuario.email === this.auth.email();
  }

  cambiarRol(usuario: Usuario, nuevoRol: ROL): void {
    if (nuevoRol === usuario.rolUsuario) return;

    this.marcarGuardando(usuario.id, true);
    this.usuario_service.cambiarRol(usuario.id, nuevoRol).subscribe({
      next: (actualizado) => {
        this.usuarios.update((lista) =>
          lista?.map((u) => (u.id === actualizado.id ? actualizado : u)) ?? lista,
        );
        this.marcarGuardando(usuario.id, false);
        this.snackBar.open(`Rol de ${usuario.nombre ?? usuario.email} actualizado`, 'Cerrar', {
          duration: 3000,
          panelClass: 'snack-success',
        });
      },
      error: () => {
        this.usuarios.update((lista) => (lista ? [...lista] : lista));
        this.marcarGuardando(usuario.id, false);
        this.snackBar.open('No se pudo cambiar el rol', 'Cerrar', {
          duration: 4000,
          panelClass: 'snack-error',
        });
      },
    });
  }

  private marcarGuardando(id: number, activo: boolean): void {
    this.guardando.update((actual) => {
      const copia = new Set(actual);
      if (activo) copia.add(id);
      else copia.delete(id);
      return copia;
    });
  }

  cambiarEstado(usuario: Usuario, nuevoEstado: boolean): void {
    if (nuevoEstado === usuario.activo) return;
    this.marcarGuardando(usuario.id, true);
    this.usuario_service.cambiarEstado(usuario.id, nuevoEstado).subscribe({
      next: (actualizado) => {
        this.usuarios.update((lista) =>
          lista?.map((u) => (u.id === actualizado.id ? actualizado : u)) ?? lista,
        );
        this.marcarGuardando(usuario.id, false);
        this.snackBar.open(`${usuario.nombre ?? usuario.email} ${nuevoEstado ? 'activado' : 'desactivado'}`, 'Cerrar', {
          duration: 3000,
          panelClass: 'snack-success',
        });
      },
      error: (err) => {
        this.usuarios.update((lista) => (lista?.map((u) => (u.id === usuario.id ? usuario : u))) ?? lista);
        this.marcarGuardando(usuario.id, false);
        this.snackBar.open(err.error?.message ?? 'No se pudo cambiar el estado', 'Cerrar', {
          duration: 4000,
          panelClass: 'snack-error',
        });
      },
    });
  }
}
