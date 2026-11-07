import { Component, OnInit } from '@angular/core';
import { inject } from '@angular/core';
import { ClientServiceService } from '../../services/client-service.service';
import Vehicle from '../../models/vehicle-model';
import { RouterLink } from '@angular/router';

@Component({
  imports: [RouterLink],
  selector: 'app-list-client',
  styleUrl: './list-client.component.scss',
  templateUrl: './list-client.component.html',
})
export class ListClientComponent implements OnInit {
  private clientService: ClientServiceService = inject(ClientServiceService);
  public arrayVehicles: Vehicle[] = [];

  ngOnInit(): void {
    this.getAllVehicles()
  }

  getAllVehicles() {
    this.clientService.getVehicles().subscribe((data : any) => {
      this.arrayVehicles = data;
    })
  }
}
