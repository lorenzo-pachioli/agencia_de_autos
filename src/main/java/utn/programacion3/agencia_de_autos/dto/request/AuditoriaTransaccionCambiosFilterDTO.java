package utn.programacion3.agencia_de_autos.dto.request;

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

    @Positive(message = "El ID de transacción debe ser positivo")
    private Long transaccionId;

    private LocalDate fechaDesde;
    private LocalDate fechaHasta;

}
