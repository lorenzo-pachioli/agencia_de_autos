package utn.programacion3.agencia_de_autos.model;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import utn.programacion3.agencia_de_autos.model.enums.EstadoVehiculo;
import utn.programacion3.agencia_de_autos.model.enums.TipoCombustible;
import utn.programacion3.agencia_de_autos.model.enums.TipoTransmision;

@Entity
@Table(name = "vehiculos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vehiculo")
    private Long id;

    @Column(nullable = false, unique = true)
    private String patente;

    @Column(nullable = false)
    private Integer anio;

    @Column(nullable = false)
    private BigDecimal precio;

    @Column(nullable = false)
    private Integer kilometraje;

    @Column(nullable = false)
    private String color;

    // Estado del vehículo (disponible, vendido, reservado, etc.)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoVehiculo estado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCombustible tipoCombustible;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTransmision tipoTransmision;

    // Relación: muchos vehículos pertenecen a un modelo
    @ManyToOne
    @JoinColumn(name = "id_modelo", nullable = false)
    private Modelo modelo;

    // Relación: un vehículo puede tener muchas imágenes
    @OneToMany(mappedBy = "vehiculo")
    private List<ImagenVehiculo> imagenes;
}
