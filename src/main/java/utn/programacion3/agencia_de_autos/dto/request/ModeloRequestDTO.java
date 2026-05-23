package utn.programacion3.agencia_de_autos.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ModeloRequestDTO {

    @NotBlank(message = "El nombre del modelo es obligatorio")
    private String nombre;

    @NotNull(message = "El año es obligatorio")
    private Integer anio;

    @NotBlank(message = "La versión es obligatoria")
    private String version;

    @NotNull(message = "La marca es obligatoria")
    private Long marcaId;
}