package utn.programacion3.agencia_de_autos.model;

import jakarta.persistence.*;
import lombok.Data;
import utn.programacion3.agencia_de_autos.model.enums.EstadoVehiculo;

@Entity
@Data
public class Vehiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String patente;
    private EstadoVehiculo estadoVehiculo;
}