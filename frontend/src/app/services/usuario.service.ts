import { HttpClient } from "@angular/common/http";
import { inject, Service } from "@angular/core";
import type { Observable } from "rxjs";
import type { Usuario } from "../models/Usuario";
import { AuthService } from "./auth.service";


@Service()
export class UsuarioService {
    private readonly http=inject(HttpClient);

    private readonly Base_URL='http://localhost:8080';
    private readonly auth_service= inject(AuthService);
    
    readonly token = this.auth_service.readToken();
    getAll():Observable<Usuario[]>{
        return this.http.get<Usuario[]>(`${this.Base_URL}/`)
    }
}