package utn.programacion3.agencia_de_autos.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImagenVehiculoRequestDTO {

    @NotBlank(message = "La URL es obligatoria")
    private String url;

    @NotNull(message = "El vehículo es obligatorio")
    private Long vehiculoId;
}