import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', redirectTo: 'catalogo', pathMatch: 'full' },
  {
    path: 'catalogo',
    loadComponent: () =>
      import('./pages/catalogo/catalogo.component').then((m) => m.CatalogoComponent),
  },
  {
    path: 'vehiculos/:id',
    loadComponent: () =>
      import('./pages/vehiculo-detalle/vehiculo-detalle.component').then(
        (m) => m.VehiculoDetalleComponent,
      ),
  },
  {
    path: 'login',
    loadComponent: () =>
      import('./pages/login/login.component').then((m) => m.LoginComponent),
  },
];
