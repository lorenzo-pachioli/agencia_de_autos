package utn.programacion3.agencia_de_autos.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import utn.programacion3.agencia_de_autos.model.enums.Rol;

@Data
public class CambiarRolRequestDTO {
    @NotNull(message = "El rol es obligatorio")
    private Rol rolUsuario;
}
