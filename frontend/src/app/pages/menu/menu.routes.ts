import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./menu.component').then((m) => m.MenuComponent),
    children: [
      {
        path: '',
        pathMatch: 'full',
        loadComponent: () =>
          import('./dashboard/dashboard.component').then((m) => m.DashboardComponent),
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
      {
        path: 'imagenes',
        loadComponent: () =>
          import('./imagenes/imagenes.component').then((m) => m.ImagenesComponent),
      },
    ],
  },
];
