package utn.programacion3.agencia_de_autos.dto.response;

import io.swagger.v3.oas.annotations.media.Schema; // ◄ IMPORTANTE SUMAR EL IMPORT
import lombok.Data;
import utn.programacion3.agencia_de_autos.model.enums.Rol;

import java.time.LocalDate;

@Data
@Schema(description = "DTO que representa la respuesta estándar con la información pública y de gestión de un usuario")
public class UsuarioResponseDTO {

    @Schema(description = "Identificador único del usuario en la base de datos", example = "1")
    private Long id;

    @Schema(description = "Nombre del usuario", example = "Agustin")
    private String nombre;

    @Schema(description = "Apellido del usuario", example = "Avalos")
    private String apellido;

    @Schema(description = "Correo electrónico registrado", example = "agustin@example.com")
    private String email;

    @Schema(description = "Rol asignado en el sistema que determina sus permisos", example = "CLIENTE")
    private Rol rolUsuario;

    @Schema(description = "Estado de la cuenta (false indica que el usuario fue dado de baja de forma lógica)", example = "true")
    private boolean activo;

    @Schema(description = "Fecha en la que el usuario se registro en el sistema", example = "2026-05-22")
    private LocalDate alta;

}
