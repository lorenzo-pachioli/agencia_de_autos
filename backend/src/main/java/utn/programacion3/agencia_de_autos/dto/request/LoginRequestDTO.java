package utn.programacion3.agencia_de_autos.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {

    @Schema(description = "Correo electrónico único del usuario", example = "agustin@example.com")
    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El formato del email no es válido")
    private String email;

    @Schema(description = "Contraseña de acceso (mínimo 8 caracteres)", example = "Password123!", minLength = 8)
    @NotBlank(message = "La contraseña no puede estar vacía")
    private String password;

}
