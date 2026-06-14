package utn.programacion3.agencia_de_autos.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import utn.programacion3.agencia_de_autos.model.enums.TipoModelo;


@Getter
@Setter
public class ModeloRequestDTO {

    @NotBlank(message = "El nombre del modelo es obligatorio")
    private String nombre;

    @NotNull(message = "El año es obligatorio")
    private Integer anio;

    @NotNull(message = "El tipo de modelo es obligatorio")
    private TipoModelo tipoModelo;

    private Boolean activo;

    @NotNull(message = "La marca es obligatoria")
    private Long marcaId;
}