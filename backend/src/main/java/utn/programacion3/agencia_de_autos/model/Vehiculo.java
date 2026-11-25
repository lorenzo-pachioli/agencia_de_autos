package utn.programacion3.agencia_de_autos.model;
import org.hibernate.annotations.ColumnDefault;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
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

    // La patente no es única en la base: un vendido que se eliminó conserva su registro
    // y la misma patente puede volver a cargarse. La regla se valida en el servicio.
    @Column(nullable = false)
    private String patente;

    /** Borrado lógico. Es independiente del estado, así un vendido eliminado sigue figurando como vendido. */
    @Column(nullable = false)
    @ColumnDefault("false")
    @Builder.Default
    private Boolean eliminado = false;

    @Column(nullable = false)
    private Integer anio;

    @Column(nullable = false)
    private BigDecimal precioAdquisicion;

    @Column(nullable = false)
    private BigDecimal precioVenta;

    @Column(nullable = false)
    private Integer kilometraje;

    @Column(nullable = false)
    private String color;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoVehiculo estado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCombustible tipoCombustible;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTransmision tipoTransmision;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "id_modelo", nullable = false)
    private Modelo modelo;

    @OneToMany(mappedBy = "vehiculo")
    private List<ImagenVehiculo> imagenes;
}