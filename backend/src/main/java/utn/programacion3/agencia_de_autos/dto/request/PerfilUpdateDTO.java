package utn.programacion3.agencia_de_autos.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "DTO para que el usuario autenticado actualice su propio perfil. La contraseña es opcional: si se omite, no se modifica.")
public class PerfilUpdateDTO {

    @Schema(description = "Nombre del usuario (solo letras)", example = "Agustin")
    @NotBlank(message = "El nombre es obligatorio")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "El nombre solo puede contener letras")
    private String nombre;

    @Schema(description = "Apellido del usuario (solo letras)", example = "Avalos")
    @NotBlank(message = "El apellido es obligatorio")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "El apellido solo puede contener letras")
    private String apellido;

    @Schema(description = "Correo electrónico único del usuario", example = "agustin@example.com")
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un email válido")
    private String email;

    // Opcional: @Size no valida cuando el valor es null, así que dejar el campo vacío no cambia la contraseña
    @Schema(description = "Nueva contraseña (opcional, mínimo 8 caracteres). Si se omite, la contraseña actual no cambia.", example = "Password123!", minLength = 8)
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;
}
