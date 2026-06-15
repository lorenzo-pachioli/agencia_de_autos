package utn.programacion3.agencia_de_autos.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class TransaccionBalanceResponseDTO {

    private BigDecimal precios_final_total;
    private BigDecimal comisiones_total;
    private BigDecimal costos_vehiculos_vendidos;

    private BigDecimal ingreso_final;

    private LocalDate fecha_desde;
    private LocalDate fecha_hasta;
}
