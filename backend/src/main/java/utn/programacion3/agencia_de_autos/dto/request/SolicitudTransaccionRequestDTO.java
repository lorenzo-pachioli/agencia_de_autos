package utn.programacion3.agencia_de_autos.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import utn.programacion3.agencia_de_autos.model.enums.MetodoPago;

@Getter
public class SolicitudTransaccionRequestDTO {

    @Schema(description = "Observaciones o notas adicionales sobre la solicitud", example = "Quiero visitar la agencia el sábado por la mañana")
    private String observaciones;

    @Schema(description = "Método de pago con el que el cliente piensa operar (opcional; lo completa el vendedor al atender)", example = "EFECTIVO",
            allowableValues = {"EFECTIVO", "TRANSFERENCIA", "PLAN_AHORRO", "CUOTA_BANCARIZADA"})
    private MetodoPago metodoPago;

    @Schema(description = "Identificador único del vehículo por el que se consulta", example = "7", minimum = "1")
    @NotNull(message = "El vehiculo_id no puede estar vacio")
    private Long vehiculo_id;
}
