import { Component, input, output } from '@angular/core';
import { Vehiculo } from '../../models/Vehiculo';
import { MatCardModule } from '@angular/material/card';
import { CurrencyPipe } from '@angular/common';

@Component({
  imports: [MatCardModule, CurrencyPipe],
  selector: 'app-vehiculo-card',
  styleUrl: './vehiculo-card.component.scss',
  templateUrl: './vehiculo-card.component.html',
})
export class VehiculoCardComponent {
  readonly vehiculo = input<Vehiculo>();
  readonly irDetalle = output<number>();
}
