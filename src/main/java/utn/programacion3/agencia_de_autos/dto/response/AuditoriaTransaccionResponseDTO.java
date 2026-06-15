package utn.programacion3.agencia_de_autos.dto.response;

import lombok.*;
import utn.programacion3.agencia_de_autos.model.enums.EstadoTransaccion;
import utn.programacion3.agencia_de_autos.model.enums.MetodoPago;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuditoriaTransaccionResponseDTO {

    private Long id;

    private Long transaccion_id;

    private Long vendedor_id;

    private LocalDate created_at = LocalDate.now();

    private BigDecimal precioFinalAnterior;
    private BigDecimal precioFinalNuevo;

    private MetodoPago metodoPagoAnterior;

    private MetodoPago metodoPagoNuevo;

    private EstadoTransaccion estadoAnterior;

    private EstadoTransaccion estadoNuevo;
}
