package utn.programacion3.agencia_de_autos.dto.request;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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

    private Long disponibles;

    private Long reservados;

    private Long vendidos;

    private Long baja;

    private Long total;

}