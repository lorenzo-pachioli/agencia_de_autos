package utn.programacion3.agencia_de_autos.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Vehiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String patente;
}