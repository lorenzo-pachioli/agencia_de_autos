import { Component, inject, signal } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { AuthService } from '../../services/auth.service';
import type { ApiError } from '../../models/ApiError';

@Component({
  imports: [
    ReactiveFormsModule,
    RouterLink,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
  ],
  selector: 'app-login',
  styleUrl: './login.component.scss',
  templateUrl: './login.component.html',
})
export class LoginComponent {
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);

  readonly form = new FormGroup({
    email: new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.email] }),
    password: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
  });

  readonly error = signal('');
  readonly cargando = signal(false);
  readonly ocultarPassword = signal(true);

  ingresar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.error.set('');
    this.cargando.set(true);

    this.auth.login(this.form.getRawValue()).subscribe({
      next: () => this.router.navigate([this.auth.isAdmin() ? '/dashboard' : '/catalogo']),
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

    this.error.set((err.error as ApiError)?.message ?? 'Email o contraseña incorrectos.');
  }
}
