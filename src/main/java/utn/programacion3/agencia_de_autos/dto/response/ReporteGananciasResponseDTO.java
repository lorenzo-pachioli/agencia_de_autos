package utn.programacion3.agencia_de_autos.dto.response;

import lombok.*;
import utn.programacion3.agencia_de_autos.dto.request.ReporteGananciaVehiculoDTO;

import java.math.BigDecimal;
import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReporteGananciasResponseDTO {

    private BigDecimal totalInvertido;

    private BigDecimal totalVenta;

    private BigDecimal gananciaTotal;

    private List<ReporteGananciaVehiculoDTO> vehiculos;

}
