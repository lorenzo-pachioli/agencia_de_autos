package utn.programacion3.agencia_de_autos.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import utn.programacion3.agencia_de_autos.model.enums.MetodoPago;

@Getter
public class SolicitudTransaccionRequestDTO {

    @Schema(description = "Observaciones o notas adicionales sobre la solicitud", example = "Quiero visitar la agencia el sábado por la mañana")
    private String observaciones;

    @Schema(description = "Método de pago con el que el cliente piensa operar", example = "EFECTIVO",
            allowableValues = {"EFECTIVO", "TRANSFERENCIA", "PLAN_AHORRO", "CUOTA_BANCARIZADA"})
    @NotNull(message = "El metodoPago no puede estar vacio")
    private MetodoPago metodoPago;

    @Schema(description = "Identificador único del vehículo por el que se consulta", example = "7", minimum = "1")
    @NotNull(message = "El vehiculo_id no puede estar vacio")
    private Long vehiculo_id;

    @Schema(description = "Identificador único del cliente que realiza la solicitud", example = "12", minimum = "1")
    @NotNull(message = "El cliente_id no puede estar vacio")
    private Long cliente_id;
}
