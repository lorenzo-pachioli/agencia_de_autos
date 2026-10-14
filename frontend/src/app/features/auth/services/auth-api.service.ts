import { HttpClient } from '@angular/common/http';
import { inject, Service, signal } from '@angular/core';
import { LoginRequest } from '../../../models/LoginRequest';
import { LoginResponse } from '../../../models/LoginResponse';

@Service()
export class AuthApiService {
    private readonly apiUrl = 'http://localhost:8080';
    private readonly http = inject(HttpClient);

    private readonly _user = signal<string | null>(null);
    readonly user = this._user.asReadonly();

    login(request: LoginRequest) {
        return this.http.post<LoginResponse>(`${this.apiUrl}/auth/login`, request);
    }
}
