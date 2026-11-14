import { HttpClient } from '@angular/common/http';
import { inject, Service } from '@angular/core';
import { LoginRequest } from '../models/Login';
import { LoginResponse } from '../models/Login';
import { Usuario, UsuarioRequest } from '../models/Usuario';
//hace solo llamadas http
@Service()
export class AuthApiService {
  private readonly apiUrl = 'http://localhost:8080';
  private readonly http = inject(HttpClient);

  login(request: LoginRequest) {
    return this.http.post<LoginResponse>(`${this.apiUrl}/auth/login`, request);
  }

  register(request: UsuarioRequest) {
    return this.http.post<Usuario>(`${this.apiUrl}/auth/register`, request);
  }
}
