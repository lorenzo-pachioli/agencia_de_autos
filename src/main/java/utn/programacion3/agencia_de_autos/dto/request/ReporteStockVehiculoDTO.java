package utn.programacion3.agencia_de_autos.dto.request;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({
        "disponibles",
        "reservados",
        "vendidos",
        "baja",
        "total"
})
public class ReporteStockVehiculoDTO {

    @Schema(description = "Cantidad de vehículos con estado DISPONIBLE", example = "15")
    private Long disponibles;

    @Schema(description = "Cantidad de vehículos con estado RESERVADO", example = "4")
    private Long reservados;

    @Schema(description = "Cantidad de vehículos con estado VENDIDO", example = "32")
    private Long vendidos;

    @Schema(description = "Cantidad de vehículos dados de baja (estado BAJA)", example = "2")
    private Long baja;

    @Schema(description = "Total general de vehículos en el sistema", example = "53")
    private Long total;
}