package utn.programacion3.agencia_de_autos.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MarcaRequestDTO {

    @Schema(description = "Nombre de la marca del vehículo", example = "Ford")
    @NotBlank(message = "El nombre de la marca es obligatorio")
    private String nombre;

    @Schema(description = "Indica si la marca está activa en el sistema", example = "true")
    private Boolean activo;
}
