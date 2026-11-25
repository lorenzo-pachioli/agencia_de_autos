package utn.programacion3.agencia_de_autos.dto.request;

import jakarta.validation.constraints.Email;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import utn.programacion3.agencia_de_autos.model.enums.EstadoTransaccion;
import utn.programacion3.agencia_de_autos.model.enums.MetodoPago;
import utn.programacion3.agencia_de_autos.validation.Groups;
import java.math.BigDecimal;

@Getter
public class TransaccionRequestDTO {

    @Schema(description = "Observaciones o notas adicionales sobre la transacción", example = "Cliente solicitó garantía extendida")
    @NotBlank(groups = {Groups.Actualizar.class}, message = "Las observaciones no puede estar vacias")
    private String observaciones;

    @Schema(description = "Monto entregado por el cliente. Solo se informa en una RESERVA; en la seña y la venta lo calcula el sistema",
            example = "1000000.00", minimum = "0.01")
    @DecimalMin(value = "0.01", groups = {Groups.Crear.class, Groups.Actualizar.class}, message = "El monto entregado debe ser mayor a 0")
    private BigDecimal monto_entregado;

    @Schema(description = "Método de pago utilizado en la transacción", example = "EFECTIVO",
            allowableValues = {"EFECTIVO", "TRANSFERENCIA", "PLAN_AHORRO", "CUOTA_BANCARIZADA"})
    @NotNull(groups = {Groups.Crear.class, Groups.Actualizar.class}, message = "El metodoPago no puede estar vacio")
    private MetodoPago metodoPago;

    @Schema(description = "Estado actual de la transacción", example = "RESERVA",
            allowableValues = {"RESERVA", "SENIADO", "VENDIDO", "CANCELADO"})
    @NotNull(groups = {Groups.Crear.class, Groups.Actualizar.class}, message = "El estadoTransaccion no puede estar vacio")
    private EstadoTransaccion estadoTransaccion;

    @Schema(description = "Identificador único del vehículo asociado a la transacción", example = "7", minimum = "1")
    @NotNull(groups = {Groups.Crear.class, Groups.Actualizar.class}, message = "El vehiculo_id no puede estar vacio")
    private Long vehiculo_id;

    @Schema(description = "Identificador del cliente. Al crear se puede omitir y enviar cliente_email", example = "12", minimum = "1")
    @NotNull(groups = {Groups.Actualizar.class}, message = "El cliente_id no puede estar vacio")
    private Long cliente_id;

    @Schema(description = "Solo al crear: email de un cliente que todavía no está cargado", example = "cliente@correo.com")
    @Email(groups = {Groups.Crear.class}, message = "El email del cliente no es válido")
    private String cliente_email;

    @Schema(description = "Identificador único del vendedor que gestiona la transacción", example = "5", minimum = "1")
    @NotNull(groups = {Groups.Crear.class, Groups.Actualizar.class}, message = "El vendedor_id no puede estar vacio")
    private Long vendedor_id;
}

