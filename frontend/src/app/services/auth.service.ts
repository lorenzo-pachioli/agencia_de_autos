import { computed, inject, Service, signal } from '@angular/core';
import { tap } from 'rxjs';
import { LoginRequest } from '../models/Login';
import { ROL, UsuarioRequest } from '../models/Usuario';
import { AuthApiService } from './auth-api.service';
//maneja la sesion
const TOKEN_KEY = 'token';

interface TokenClaims {
  sub: string;
  role: string;
  exp: number;
}

@Service()
export class AuthService {
  private readonly authApi = inject(AuthApiService);

  private readonly _token = signal<string | null>(this.readToken());
  readonly token = this._token.asReadonly();
  readonly isLoggedIn = computed(() => this._token() !== null);

  
  readonly rol = computed<ROL | null>(() => {
    const claimRol = this.decodeToken(this._token())?.role?.replace('ROLE_', '');
    return (Object.values(ROL) as string[]).includes(claimRol ?? '') ? (claimRol as ROL) : null;
  });

  readonly email = computed(() => this.decodeToken(this._token())?.sub ?? null);

  readonly isAdmin = computed(() => this.rol() === ROL.ADMINISTRADOR);

  readonly isVendedor = computed(() => this.rol() === ROL.VENDEDOR);

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

  private decodeToken(token: string | null): TokenClaims | null {
    if (!token) return null;
    try {
      const payload = token.split('.')[1];
      const base64 = payload.replace(/-/g, '+').replace(/_/g, '/');
      const json = decodeURIComponent(
        atob(base64)
          .split('')
          .map((c) => '%' + c.charCodeAt(0).toString(16).padStart(2, '0'))
          .join(''),
      );
      return JSON.parse(json) as TokenClaims;
    } catch {
      return null;
    }
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
