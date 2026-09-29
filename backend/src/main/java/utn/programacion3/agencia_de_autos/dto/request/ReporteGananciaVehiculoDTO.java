package utn.programacion3.agencia_de_autos.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReporteGananciaVehiculoDTO {

    @Schema(description = "Patente del vehículo vendido", example = "AB123CD")
    private String patente;

    @Schema(description = "Nombre del modelo del vehículo", example = "Focus")
    private String modelo;

    @Schema(description = "Precio al que fue adquirido el vehículo", example = "8500000.00", minimum = "0")
    private BigDecimal precioAdquisicion;

    @Schema(description = "Precio al que fue vendido el vehículo", example = "11200000.00", minimum = "0")
    private BigDecimal precioVenta;

    @Schema(description = "Ganancia neta obtenida en la venta (precioVenta - precioAdquisicion)", example = "2700000.00")
    private BigDecimal ganancia;
}

