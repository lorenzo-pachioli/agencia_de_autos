package utn.programacion3.agencia_de_autos.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Generated;
import utn.programacion3.agencia_de_autos.model.enums.Rol;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String apellido;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Rol rolUsuario;

    private boolean activo;

    private LocalDate alta;

    @ManyToMany
    @JoinTable(
            name = "favorito",
            joinColumns = @JoinColumn(name = "cliente_id"),
            inverseJoinColumns = @JoinColumn(name = "vehiculo_id")
    )
    private List<Vehiculo> favoritos;

    // Nos ahorramos setear a mano en el service la fecha cuando se crea un usuario
    @PrePersist
    protected void onCreate() {
        alta = LocalDate.now();
    }


}
