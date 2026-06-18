package utn.programacion3.agencia_de_autos.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImagenVehiculoRequestDTO {

    @Schema(description = "URL pública de la imagen del vehículo", example = "https://cdn.agencia.com/autos/ford-focus-2022.jpg")
    @NotBlank(message = "La URL es obligatoria")
    private String url;

    @Schema(description = "Identificador único del vehículo al que pertenece la imagen", example = "7", minimum = "1")
    @NotNull(message = "El vehículo es obligatorio")
    private Long vehiculoId;
}