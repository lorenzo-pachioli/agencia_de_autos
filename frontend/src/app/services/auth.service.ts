import { computed, inject, Service, signal } from '@angular/core';
import { tap } from 'rxjs';
import { LoginRequest } from '../models/LoginRequest';
import { UsuarioRequest } from '../models/Usuario';
import { AuthApiService } from './auth-api.service';
//maneja la sesion
const TOKEN_KEY = 'token';

@Service()
export class AuthService {
  private readonly authApi = inject(AuthApiService);

  private readonly _token = signal<string | null>(this.readToken());
  readonly token = this._token.asReadonly();
  readonly isLoggedIn = computed(() => this._token() !== null);

  login(request: LoginRequest) {
    return this.authApi.login(request).pipe(
      tap((response) => {
        this.saveToken(response.token);
        this._token.set(response.token);
      }),
    );
  }

  register(request: UsuarioRequest) {
    return this.authApi.register(request);
  }

  logout() {
    try {
      localStorage.removeItem(TOKEN_KEY);
    } catch {}
    this._token.set(null);
  }

  private readToken(): string | null {
    try {
      return localStorage.getItem(TOKEN_KEY);
    } catch {
      return null;
    }
  }

  private saveToken(token: string) {
    try {
      localStorage.setItem(TOKEN_KEY, token);
    } catch {}
  }
}
