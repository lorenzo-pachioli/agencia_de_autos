import { Component, inject, signal } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { UsuarioService } from '../../../services/usuario.service';
import type { PerfilUpdateRequest, Usuario } from '../../../models/Usuario';
import type { ApiError } from '../../../models/ApiError';

// Mismo patrón que valida el backend (nombre/apellido solo letras)
const NOMBRE_PATTERN = /^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$/;

// La confirmación solo debe coincidir cuando el usuario escribió una contraseña nueva
function passwordsIgualesValidator(): ValidatorFn {
  return (grupo: AbstractControl): ValidationErrors | null => {
    const password = grupo.get('password')?.value;
    const confirmar = grupo.get('confirmarPassword')?.value;
    if (!password) return null;
    return password !== confirmar ? { passwordsDistintas: true } : null;
  };
}

@Component({
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
  ],
  selector: 'app-perfil',
  styleUrl: './perfil.component.scss',
  templateUrl: './perfil.component.html',
})
export class PerfilComponent {
  private readonly usuarioService = inject(UsuarioService);

  readonly form = new FormGroup(
    {
      nombre: new FormControl('', {
        nonNullable: true,
        validators: [Validators.required, Validators.pattern(NOMBRE_PATTERN)],
      }),
      apellido: new FormControl('', {
        nonNullable: true,
        validators: [Validators.required, Validators.pattern(NOMBRE_PATTERN)],
      }),
      email: new FormControl('', {
        nonNullable: true,
        validators: [Validators.required, Validators.email],
      }),
      // Opcionales: la contraseña solo se cambia si se completa
      password: new FormControl('', { nonNullable: true, validators: [Validators.minLength(8)] }),
      confirmarPassword: new FormControl('', { nonNullable: true }),
    },
    { validators: passwordsIgualesValidator() },
  );

  readonly usuario = signal<Usuario | null>(null);
  readonly cargandoPerfil = signal(true);
  readonly error = signal('');
  readonly guardando = signal(false);
  readonly guardado = signal(false);
  readonly errorCarga = signal('');
  readonly ocultarPassword = signal(true);
  readonly ocultarConfirmar = signal(true);

  constructor() {
    this.cargarPerfil();
  }

  cargarPerfil(): void {
    this.cargandoPerfil.set(true);
    this.errorCarga.set('');
    this.usuarioService.getMe().subscribe({
      next: (usuario) => {
        this.usuario.set(usuario);
        this.form.patchValue({
          nombre: usuario.nombre,
          apellido: usuario.apellido,
          email: usuario.email,
        });
        this.cargandoPerfil.set(false);
      },
      error: (err: HttpErrorResponse) => {
        this.cargandoPerfil.set(false);
        this.errorCarga.set(
          err.status === 0
            ? 'No se pudo conectar con el servidor.'
            : 'No se pudo cargar tu perfil. Intentá nuevamente.',
        );
      },
    });
  }

  guardar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.error.set('');
    this.guardado.set(false);
    this.guardando.set(true);

    const { nombre, apellido, email, password } = this.form.getRawValue();
    const perfil: PerfilUpdateRequest = { nombre, apellido, email };
    // Solo enviamos la contraseña si el usuario la completó
    if (password) {
      perfil.password = password;
    }

    this.usuarioService.updateMe(perfil).subscribe({
      next: (usuario) => {
        this.usuario.set(usuario);
        this.form.patchValue({ password: '', confirmarPassword: '' });
        this.form.markAsPristine();
        this.guardando.set(false);
        this.guardado.set(true);
      },
      error: (err: HttpErrorResponse) => {
        this.guardando.set(false);
        this.manejarError(err);
      },
    });
  }

  private manejarError(err: HttpErrorResponse): void {
    if (err.status === 0) {
      this.error.set('No se pudo conectar con el servidor.');
      return;
    }

    const apiError = err.error as ApiError | undefined;

    // 400: errores de validación por campo (MethodArgumentNotValidException en el backend)
    if (apiError?.validationErrors) {
      Object.entries(apiError.validationErrors).forEach(([campo, mensaje]) => {
        this.form.get(campo)?.setErrors({ backend: mensaje });
      });
      this.error.set('Revisá los datos marcados en rojo.');
      return;
    }

    // 409: email ya registrado por otro usuario (EmailAlreadyExistsException)
    if (err.status === 409) {
      const mensaje = apiError?.message ?? 'Ese email ya está en uso por otro usuario.';
      this.form.get('email')?.setErrors({ backend: mensaje });
      this.error.set(mensaje);
      return;
    }

    this.error.set(apiError?.message ?? 'Ocurrió un error inesperado.');
  }
}
