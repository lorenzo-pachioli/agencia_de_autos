package utn.programacion3.agencia_de_autos.dto.request;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
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

    @Positive(message = "El ID de transacción debe ser positivo")
    private Long transaccionId;

    @Positive(message = "El ID de vendedor debe ser positivo")
    private Long vendedorId;

    private LocalDate fechaDesde;
    private LocalDate fechaHasta;

    @PositiveOrZero(message = "El precio final anterior no puede ser negativo")
    private BigDecimal precioFinalAnterior;

    @PositiveOrZero(message = "El precio final nuevo no puede ser negativo")
    private BigDecimal precioFinalNuevo;

    private MetodoPago metodoPagoAnterior;
    private MetodoPago metodoPagoNuevo;

    private EstadoTransaccion estadoAnterior;
    private EstadoTransaccion estadoNuevo;

    private Pageable pageable;
}
