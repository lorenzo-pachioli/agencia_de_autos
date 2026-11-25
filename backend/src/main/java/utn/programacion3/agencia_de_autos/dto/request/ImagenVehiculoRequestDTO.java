package utn.programacion3.agencia_de_autos.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import utn.programacion3.agencia_de_autos.validation.ImagenValida;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImagenVehiculoRequestDTO {

    @Schema(description = "Imagen JPG, PNG o WEBP del vehículo codificada como data URI base64", example = "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQ...")
    @NotBlank(message = "La imagen es obligatoria")
    @ImagenValida
    private String url;

    @Schema(description = "Identificador único del vehículo al que pertenece la imagen", example = "7", minimum = "1")
    @NotNull(message = "El vehículo es obligatorio")
    private Long vehiculoId;

    @Schema(description = "Indica si esta es la imagen principal del vehículo", example = "false")
    @Builder.Default
    private Boolean esPrincipal = false;
}