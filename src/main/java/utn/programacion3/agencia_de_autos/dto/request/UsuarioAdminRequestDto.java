package utn.programacion3.agencia_de_autos.dto.request;

import io.swagger.v3.oas.annotations.media.Schema; // ◄ IMPORTANTE SUMAR ESTE IMPORT
import jakarta.validation.constraints.*;
import lombok.Data;
import utn.programacion3.agencia_de_autos.model.enums.Rol;

@Data
@Schema(description = "DTO utilizado por el Administrador para crear o modificar usuarios asignando roles específicos")
public class UsuarioAdminRequestDto {

    @Schema(description = "Nombre del usuario gestionado", example = "Emanuel")
    @NotBlank(message = "El nombre es obligatorio")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "El nombre solo puede contener letras")
    private String nombre;

    @Schema(description = "Apellido del usuario gestionado", example = "Perez")
    @NotBlank(message = "El apellido es obligatorio")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "El nombre solo puede contener letras")
    private String apellido;

    @Schema(description = "Correo electrónico único del usuario gestionado", example = "emanuel@agencia.com")
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un email válido")
    private String email;

    @Schema(description = "Contraseña temporal o asignada", example = "AdminSecure2026", minLength = 8)
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;

    @Schema(description = "Rol asignado al usuario dentro del sistema", example = "VENDEDOR", allowableValues = {"VENDEDOR"})
    @NotNull(message = "El rol es obligatorio para el administrador")
    private Rol rolUsuario;
}
