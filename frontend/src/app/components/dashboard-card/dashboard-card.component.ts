import { CurrencyPipe } from '@angular/common';
import { Component, input } from '@angular/core';
import { MatCardModule } from '@angular/material/card';

export interface DashboardCardData {
  titulo: string;
  valor: number;
  descripcion: string;
  moneda?: boolean;
}

@Component({
  imports: [MatCardModule, CurrencyPipe],
  selector: 'app-dashboard-card',
  styleUrl: './dashboard-card.component.scss',
  templateUrl: './dashboard-card.component.html',
})
export class DashboardCardComponent {
  readonly card = input.required<DashboardCardData>();
}
