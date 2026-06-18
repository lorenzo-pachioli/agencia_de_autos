package utn.programacion3.agencia_de_autos.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import utn.programacion3.agencia_de_autos.model.enums.EstadoVehiculo;

@Getter
@Setter
public class CambiarEstadoVehiculoDTO {

    @Schema(description = "Nuevo estado a asignar al vehículo", example = "DISPONIBLE",
            allowableValues = {"DISPONIBLE", "RESERVADO", "VENDIDO", "BAJA"})
    @NotNull(message = "El estado es obligatorio")
    private EstadoVehiculo estado;
}

