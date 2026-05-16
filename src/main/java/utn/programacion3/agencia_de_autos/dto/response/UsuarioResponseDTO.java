package utn.programacion3.agencia_de_autos.dto.response;

import lombok.Data;
import utn.programacion3.agencia_de_autos.model.enums.Rol;

import java.time.LocalDate;

@Data
public class UsuarioResponseDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private Rol rolUsuario;
    private boolean activo;
    private LocalDate alta;

}
