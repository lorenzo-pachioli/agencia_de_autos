package utn.programacion3.agencia_de_autos.dto.request;

import lombok.*;
import org.springframework.data.domain.Pageable;
import utn.programacion3.agencia_de_autos.model.enums.EstadoTransaccion;
import utn.programacion3.agencia_de_autos.model.enums.MetodoPago;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuditoriaTransaccionFilterDTO {

    private Long transaccionId;
    private Long vendedorId;

    private LocalDate fechaDesde;
    private LocalDate fechaHasta;

    private BigDecimal precioFinalAnterior;
    private BigDecimal precioFinalNuevo;

    private MetodoPago metodoPagoAnterior;
    private MetodoPago metodoPagoNuevo;

    private EstadoTransaccion estadoAnterior;
    private EstadoTransaccion estadoNuevo;

    private Pageable pageable;
}
