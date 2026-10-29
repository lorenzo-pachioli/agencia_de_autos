import { Routes } from '@angular/router';
import { FormClientComponent } from './pages/form-client/form-client.component';
import { ListClientComponent } from './pages/list-client/list-client.component';
import { CarDetailsComponent } from './pages/car-details/car-details.component';

export const routes: Routes = [
    {path: 'form-client', component: FormClientComponent},
    {path: 'list-client', component: ListClientComponent},
    {path: 'list-client/:id', component: CarDetailsComponent},
];
