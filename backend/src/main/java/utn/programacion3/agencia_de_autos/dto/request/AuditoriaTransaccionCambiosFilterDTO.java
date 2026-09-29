package utn.programacion3.agencia_de_autos.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuditoriaTransaccionCambiosFilterDTO {

    @Schema(description = "ID único de la transacción a consultar", example = "1025")
    @Positive(message = "El ID de transacción debe ser positivo")
    private Long transaccionId;

    @Schema(description = "Fecha inicial del rango de auditoría (YYYY-MM-DD)", example = "2026-01-01")
    private LocalDate fechaDesde;

    @Schema(description = "Fecha final del rango de auditoría (YYYY-MM-DD)", example = "2026-01-31")
    private LocalDate fechaHasta;
}
