package utn.programacion3.agencia_de_autos.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import utn.programacion3.agencia_de_autos.model.enums.EstadoVehiculo;

@Getter
@Setter
public class CambiarEstadoVehiculoDTO {

        @NotNull(message = "El estado es obligatorio")
        private EstadoVehiculo estado;
}

