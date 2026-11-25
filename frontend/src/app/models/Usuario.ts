export interface Usuario {
  id: number;
  // null mientras el cliente cargado por la agencia no se registra
  nombre: string | null;
  apellido: string | null;
  email: string;
  rolUsuario: ROL;
  activo: boolean;
  registrado: boolean;
  alta: string;
}

export interface UsuarioRequest {
  nombre: string;
  apellido: string;
  email: string;
  password: string;
}

export interface PerfilUpdateRequest {
  nombre: string;
  apellido: string;
  email: string;
  // Opcional: si se omite, el backend no modifica la contraseña actual
  password?: string;
}

export enum ROL {
  VENDEDOR = "VENDEDOR",
  ADMINISTRADOR = "ADMINISTRADOR",
  USUARIO = "USUARIO",
}
