package utn.programacion3.agencia_de_autos.model;
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

    // Relación: una marca puede tener muchos modelos
    @OneToMany(mappedBy = "marca")
    private List<Modelo> modelos;
}
