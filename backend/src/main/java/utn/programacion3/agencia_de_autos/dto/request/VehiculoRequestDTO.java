package utn.programacion3.agencia_de_autos.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import utn.programacion3.agencia_de_autos.model.enums.EstadoVehiculo;
import utn.programacion3.agencia_de_autos.model.enums.TipoCombustible;
import utn.programacion3.agencia_de_autos.model.enums.TipoTransmision;

import java.math.BigDecimal;

@Getter
@Setter
public class VehiculoRequestDTO {

    @Schema(description = "Patente del vehículo (formato argentino)", example = "AB123CD")
    @NotBlank(message = "La patente es obligatoria")
    private String patente;

    @Schema(description = "Año de fabricación del vehículo", example = "2022", minimum = "1900")
    @NotNull(message = "El año es obligatorio")
    private Integer anio;

    @Schema(description = "Precio de adquisición del vehículo por la agencia", example = "8500000.00", minimum = "0")
    @NotNull(message = "El precio de adquisición es obligatorio")
    private BigDecimal precioAdquisicion;

    @Schema(description = "Precio de venta al público", example = "11200000.00", minimum = "0")
    @NotNull(message = "El precio de venta es obligatorio")
    private BigDecimal precioVenta;

    @Schema(description = "Identificador único del modelo del vehículo", example = "5", minimum = "1")
    @NotNull(message = "El modelo es obligatorio")
    private Long modeloId;

    @Schema(description = "Kilometraje actual del vehículo", example = "45000", minimum = "0")
    @NotNull
    private Integer kilometraje;

    @Schema(description = "Color del vehículo", example = "Blanco")
    @NotBlank
    private String color;

    @Schema(description = "Descripción adicional o características del vehículo", example = "Vehículo en excelente estado, único dueño")
    private String descripcion;

    @Schema(description = "Estado actual del vehículo en el inventario", example = "DISPONIBLE",
            allowableValues = {"DISPONIBLE", "RESERVADO", "VENDIDO", "BAJA"})
    @NotNull
    private EstadoVehiculo estado;

    @Schema(description = "Tipo de transmisión del vehículo", example = "MANUAL",
            allowableValues = {"MANUAL", "AUTOMATICA"})
    @NotNull
    private TipoTransmision tipoTransmision;

    @Schema(description = "Tipo de combustible del vehículo", example = "NAFTA",
            allowableValues = {"NAFTA", "DIESEL", "HIBRIDO", "ELECTRICO"})
    @NotNull
    private TipoCombustible tipoCombustible;
}
