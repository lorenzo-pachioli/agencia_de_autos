package utn.programacion3.agencia_de_autos.dto.request;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import utn.programacion3.agencia_de_autos.model.enums.EstadoVehiculo;
import utn.programacion3.agencia_de_autos.model.enums.TipoCombustible;
import utn.programacion3.agencia_de_autos.model.enums.TipoTransmision;

import java.math.BigDecimal;

@Getter
@Setter
public class VehiculoFilterDTO {

    @Positive(message = "La marcaId debe ser un número positivo")
    private Long marcaId;

    @Positive(message = "El modeloId debe ser un número positivo")
    private Long modeloId;

    private TipoCombustible combustible;

    private TipoTransmision tipoTransmision;

    private EstadoVehiculo estado;

    private String color;

    @PositiveOrZero
    private BigDecimal minPrecio;

    @PositiveOrZero
    private BigDecimal maxPrecio;

    @PositiveOrZero
    private Integer minAnio;

    @PositiveOrZero
    private Integer maxAnio;
}