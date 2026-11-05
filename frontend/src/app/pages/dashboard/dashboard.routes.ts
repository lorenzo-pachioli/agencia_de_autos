import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./dashboard.component').then((m) => m.DashboardComponent),
    children: [
      {
        path: '',
        pathMatch: 'full',
        loadComponent: () =>
          import('./inicio/inicio.component').then((m) => m.InicioComponent),
      },
      {
        path: 'marcas',
        loadComponent: () =>
          import('./marcas/marcas.component').then((m) => m.MarcasComponent),
      },
      {
        path: 'modelos',
        loadComponent: () =>
          import('./modelos/modelos.component').then((m) => m.ModelosComponent),
      },
      {
        path: 'transacciones',
        loadComponent: () =>
          import('./transacciones/transacciones.component').then((m) => m.TransaccionesComponent),
      },
      {
        path: 'usuarios',
        loadComponent: () =>
          import('./usuarios/usuarios.component').then((m) => m.UsuariosComponent),
      },
      {
        path: 'vehiculos',
        loadComponent: () =>
          import('./vehiculos/vehiculos.component').then((m) => m.VehiculosComponent),
      },
    ],
  },
];
