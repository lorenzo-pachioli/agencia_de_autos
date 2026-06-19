package utn.programacion3.agencia_de_autos.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "Identificador único de la transacción original", example = "1024", minimum = "1")
    @Positive(message = "El ID de transacción debe ser positivo")
    private Long transaccionId;

    @Schema(description = "Identificador único del vendedor asociado a la transacción", example = "58", minimum = "1")
    @Positive(message = "El ID de vendedor debe ser positivo")
    private Long vendedorId;

    @Schema(description = "Fecha inicial para el rango de búsqueda (Formato YYYY-MM-DD)", example = "2026-01-01")
    private LocalDate fechaDesde;

    @Schema(description = "Fecha final para el rango de búsqueda (Formato YYYY-MM-DD)", example = "2026-01-15")
    private LocalDate fechaHasta;

    @Schema(description = "Monto del precio final que registraba la transacción antes del cambio", example = "1500.50", minimum = "0")
    @PositiveOrZero(message = "El precio final anterior no puede ser negativo")
    private BigDecimal precioFinalAnterior;

    @Schema(description = "Monto del nuevo precio final establecido tras la modificación", example = "1750.00", minimum = "0")
    @PositiveOrZero(message = "El precio final nuevo no puede ser negativo")
    private BigDecimal precioFinalNuevo;

    @Schema(description = "Método de pago original antes de ser modificado", example = "EFECTIVO")
    private MetodoPago metodoPagoAnterior;

    @Schema(description = "Nuevo método de pago asignado a la transacción", example = "TARJETA_CREDITO")
    private MetodoPago metodoPagoNuevo;

    @Schema(description = "Estado original en el que se encontraba la transacción", example = "RESERVA")
    private EstadoTransaccion estadoAnterior;

    @Schema(description = "Nuevo estado asignado a la transacción tras el cambio", example = "SENIADO")
    private EstadoTransaccion estadoNuevo;
}
