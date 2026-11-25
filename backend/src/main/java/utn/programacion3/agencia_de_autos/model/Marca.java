package utn.programacion3.agencia_de_autos.model;
import org.hibernate.annotations.ColumnDefault;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "marcas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Marca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_marca")
    private Long id;

    // Nombre de la marca
    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;

    /** Borrado lógico. "activo" es otra cosa: lo cambia el usuario y sigue visible. */
    @Column(nullable = false)
    @ColumnDefault("false")
    @Builder.Default
    private Boolean eliminado = false;

    // Relación: una marca puede tener muchos modelos
    @OneToMany(mappedBy = "marca")
    private List<Modelo> modelos;
}
