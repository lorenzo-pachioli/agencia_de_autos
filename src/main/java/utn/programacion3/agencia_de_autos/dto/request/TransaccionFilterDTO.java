package utn.programacion3.agencia_de_autos.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import utn.programacion3.agencia_de_autos.model.enums.EstadoTransaccion;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class TransaccionFilterDTO {

    @Schema(description = "Identificador único del vehículo a filtrar", example = "3", minimum = "1")
    @Positive(message = "El vehiculo_id debe ser un numero positivo")
    private Long vehiculo_id;

    @Schema(description = "Identificador único del cliente a filtrar", example = "1", minimum = "1")
    @Positive(message = "El cliente_id debe ser un numero positivo")
    private Long cliente_id;

    @Schema(description = "Identificador único del vendedor a filtrar", example = "3", minimum = "1")
    @Positive(message = "El vendedor_id debe ser un numero positivo")
    private Long vendedor_id;

    @Schema(description = "Fecha inicial del rango de búsqueda (Formato YYYY-MM-DD)", example = "2026-01-01")
    @PastOrPresent(message = "La fecha desde no puede ser futura")
    private LocalDate fechaDesde;

    @Schema(description = "Fecha final del rango de búsqueda (Formato YYYY-MM-DD)", example = "2026-06-18")
    @PastOrPresent(message = "La fecha hasta no puede ser futura")
    private LocalDate fechaHasta;

    @Schema(description = "Estado de la transacción a filtrar", example = "RESERVA",
            allowableValues = {"RESERVA", "SENIADO", "VENDIDO", "CANCELADO"})
    private EstadoTransaccion estadoTransaccion;
}