package utn.programacion3.agencia_de_autos.model;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "imagenes_vehiculo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImagenVehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_imagen")
    private Long id;

    @Column(nullable = false)
    private String url;

    // Relación: muchas imágenes pertenecen a un vehículo
    @ManyToOne
    @JoinColumn(name = "id_vehiculo", nullable = false)
    private Vehiculo vehiculo;

    @Column(nullable = false)
    @Builder.Default
    private Boolean esPrincipal = false;
}
