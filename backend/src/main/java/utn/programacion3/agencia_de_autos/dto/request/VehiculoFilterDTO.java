package utn.programacion3.agencia_de_autos.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "Identificador único de la marca para filtrar", example = "2", minimum = "1")
    @Positive(message = "La marcaId debe ser un número positivo")
    private Long marcaId;

    @Schema(description = "Identificador único del modelo para filtrar", example = "5", minimum = "1")
    @Positive(message = "El modeloId debe ser un número positivo")
    private Long modeloId;

    @Schema(description = "Tipo de combustible del vehículo", example = "NAFTA",
            allowableValues = {"NAFTA", "DIESEL", "HIBRIDO", "ELECTRICO"})
    private TipoCombustible combustible;

    @Schema(description = "Tipo de transmisión del vehículo", example = "MANUAL",
            allowableValues = {"MANUAL", "AUTOMATICA"})
    private TipoTransmision tipoTransmision;

    @Schema(description = "Estado actual del vehículo", example = "DISPONIBLE",
            allowableValues = {"DISPONIBLE", "RESERVADO", "VENDIDO", "BAJA"})
    private EstadoVehiculo estado;

    @Schema(description = "Color del vehículo a filtrar", example = "Rojo")
    private String color;

    @Schema(description = "Precio mínimo de venta (inclusive)", example = "5000000.00", minimum = "0")
    @PositiveOrZero
    private BigDecimal minPrecio;

    @Schema(description = "Precio máximo de venta (inclusive)", example = "15000000.00", minimum = "0")
    @PositiveOrZero
    private BigDecimal maxPrecio;

    @Schema(description = "Año mínimo de fabricación (inclusive)", example = "2018", minimum = "0")
    @PositiveOrZero
    private Integer minAnio;

    @Schema(description = "Año máximo de fabricación (inclusive)", example = "2024", minimum = "0")
    @PositiveOrZero
    private Integer maxAnio;
}