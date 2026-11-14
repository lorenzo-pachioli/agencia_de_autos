package utn.programacion3.agencia_de_autos.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AsignarVisitaRequestDTO {

    @Schema(description = "Fecha y hora acordada para la visita (opcional; sin fecha solo se asigna el vendedor)", example = "2026-11-20T10:30:00")
    @Future(message = "La fecha de visita debe ser futura")
    private LocalDateTime fechaVisita;
}
