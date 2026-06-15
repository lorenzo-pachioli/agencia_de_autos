package utn.programacion3.agencia_de_autos.dto.request;

import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReporteGananciaVehiculoDTO {

        private String patente;

        private String modelo;

        private BigDecimal precioAdquisicion;

        private BigDecimal precioVenta;

        private BigDecimal ganancia;

}
