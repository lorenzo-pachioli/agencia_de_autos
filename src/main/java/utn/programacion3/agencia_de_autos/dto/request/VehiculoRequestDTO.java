package utn.programacion3.agencia_de_autos.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehiculoRequestDTO {

    @NotNull(message = "El modelo es obligatorio")
    private Long modeloId;
}