package utn.programacion3.agencia_de_autos.dto.request;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import utn.programacion3.agencia_de_autos.model.enums.EstadoVehiculo;
import utn.programacion3.agencia_de_autos.model.enums.TipoCombustible;

import java.math.BigDecimal;

@Getter
@Setter
public class VehiculoFilterDTO {

    @Positive(message = "La marcaId debe ser un número positivo")
    private Long marcaId;

    @Positive(message = "El modeloId debe ser un número positivo")
    private Long modeloId;

    private TipoCombustible combustible;

    private EstadoVehiculo estado;

    @PositiveOrZero(message = "El precio mínimo no puede ser negativo")
    private BigDecimal minPrecio;

    @PositiveOrZero(message = "El precio máximo no puede ser negativo")
    private BigDecimal maxPrecio;
}