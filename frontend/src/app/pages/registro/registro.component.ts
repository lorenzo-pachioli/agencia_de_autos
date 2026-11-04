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
import { RouterLink } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { AuthService } from '../../services/auth.service';
import type { ApiError } from '../../models/ApiError';

// Mismo patrón que valida el backend en UsuarioRequestDTO (nombre/apellido solo letras)
const NOMBRE_PATTERN = /^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$/;

function passwordsIgualesValidator(): ValidatorFn {
  return (grupo: AbstractControl): ValidationErrors | null => {
    const password = grupo.get('password')?.value;
    const confirmar = grupo.get('confirmarPassword')?.value;
    return password && confirmar && password !== confirmar ? { passwordsDistintas: true } : null;
  };
}

@Component({
  imports: [
    ReactiveFormsModule,
    RouterLink,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
  ],
  selector: 'app-registro',
  styleUrl: './registro.component.scss',
  templateUrl: './registro.component.html',
})
export class RegistroComponent {
  private readonly auth = inject(AuthService);

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
      password: new FormControl('', {
        nonNullable: true,
        validators: [Validators.required, Validators.minLength(8)],
      }),
      confirmarPassword: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    },
    { validators: passwordsIgualesValidator() },
  );

  readonly error = signal('');
  readonly cargando = signal(false);
  readonly registrado = signal(false);
  readonly ocultarPassword = signal(true);
  readonly ocultarConfirmar = signal(true);

  registrarse(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.error.set('');
    this.cargando.set(true);

    const { confirmarPassword, ...usuario } = this.form.getRawValue();

    this.auth.register(usuario).subscribe({
      next: () => {
        this.cargando.set(false);
        this.registrado.set(true);
      },
      error: (err: HttpErrorResponse) => {
        this.cargando.set(false);
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

    // 409: email ya registrado (EmailAlreadyExistsException)
    if (err.status === 409) {
      const mensaje = apiError?.message ?? 'Ese email ya está registrado.';
      this.form.get('email')?.setErrors({ backend: mensaje });
      this.error.set(mensaje);
      return;
    }

    this.error.set(apiError?.message ?? 'Ocurrió un error inesperado.');
  }
}
