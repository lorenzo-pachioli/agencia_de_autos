import { HttpClient } from '@angular/common/http';
import { inject, Service } from '@angular/core';
import type { Observable } from 'rxjs';
import type { PerfilUpdateRequest, Usuario, UsuarioRequest } from '../models/Usuario';

// El token lo agrega authInterceptor; el backend identifica al usuario con el JWT
@Service()
export class UsuarioService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/usuarios';

  getAll(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(this.apiUrl);
  }

  getMe(): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.apiUrl}/me`);
  }

  getById(id: number): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.apiUrl}/${id}`);
  }

  updateMe(perfil: PerfilUpdateRequest): Observable<Usuario> {
    return this.http.put<Usuario>(`${this.apiUrl}/me`, perfil);
  }

  update(id: number, usuario: UsuarioRequest): Observable<Usuario> {
    return this.http.put<Usuario>(`${this.apiUrl}/${id}`, usuario);
  }

  darDeBaja(id: number): Observable<Usuario> {
    return this.http.patch<Usuario>(`${this.apiUrl}/${id}`, null);
  }

  crearVendedor(usuario: UsuarioRequest): Observable<Usuario> {
    return this.http.post<Usuario>(`${this.apiUrl}/vendedores`, usuario);
  }
}
