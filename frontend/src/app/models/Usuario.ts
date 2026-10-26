export interface Usuario {
  id: number;
  nombre: string;
  apellido: string;
  email: string;
  rolUsuario: ROL;
  activo: boolean;
  alta: string;
}

export interface UsuarioRequest {
  nombre: string;
  apellido: string;
  email: string;
  password: string;
}

export enum ROL {
  VENDEDOR = "VENDEDOR",
  ADMINISTRADOR = "ADMINISTRADOR",
  USUARIO = "USUARIO",
}
