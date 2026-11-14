import { Routes } from '@angular/router';
import { ListClientComponent } from './pages/list-client/list-client.component';
import { CarDetailsComponent } from './pages/car-details/car-details.component';

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
  {
    path: 'registro',
    loadComponent: () =>
      import('./pages/registro/registro.component').then((m) => m.RegistroComponent),
  },
  {
    path: 'menu',
    loadChildren: () =>
      import('./pages/menu/menu.routes').then((m) => m.routes),
  },
];
