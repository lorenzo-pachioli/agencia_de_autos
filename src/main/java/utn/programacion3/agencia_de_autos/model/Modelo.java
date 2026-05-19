package utn.programacion3.agencia_de_autos.model;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "modelos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Modelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_modelo")
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private Integer anio;

    @Column(nullable = false)
    private String version;

    // Relación: muchos modelos pertenecen a una marca
    @ManyToOne
    @JoinColumn(name = "id_marca")
    private Marca marca;

    // Relación: un modelo puede tener muchos vehículos
    @OneToMany(mappedBy = "modelo")
    private List<Vehiculo> vehiculos;
}