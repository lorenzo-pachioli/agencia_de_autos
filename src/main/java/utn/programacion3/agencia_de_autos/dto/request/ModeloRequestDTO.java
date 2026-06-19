package utn.programacion3.agencia_de_autos.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import utn.programacion3.agencia_de_autos.model.enums.TipoModelo;

@Getter
@Setter
public class ModeloRequestDTO {

    @Schema(description = "Nombre del modelo del vehículo", example = "Focus")
    @NotBlank(message = "El nombre del modelo es obligatorio")
    private String nombre;

    @Schema(description = "Año de fabricación del modelo", example = "2022", minimum = "1900")
    @NotNull(message = "El año es obligatorio")
    private Integer anio;

    @Schema(description = "Categoría o tipo del modelo", example = "AUTO",
            allowableValues = {"AUTO", "MOTO", "CAMIONETA", "SUV", "UTILITARIO"})
    @NotNull(message = "El tipo de modelo es obligatorio")
    private TipoModelo tipoModelo;

    @Schema(description = "Indica si el modelo está activo en el sistema", example = "true")
    private Boolean activo;

    @Schema(description = "Identificador único de la marca a la que pertenece el modelo", example = "3", minimum = "1")
    @NotNull(message = "La marca es obligatoria")
    private Long marcaId;
}