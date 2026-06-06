package utn.programacion3.agencia_de_autos.dto.request;

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

    @NotBlank(groups = {Groups.Actualizar.class}, message = "Las observaciones no puede estar vacias")
    private String observaciones;

    @NotNull(groups = {Groups.Crear.class, Groups.Actualizar.class}, message = "El precio final no puede estar vacio")
    @DecimalMin(value = "0.01", message = "El precio final debe ser mayor a 0")
    private BigDecimal precio_final;

    @NotNull(groups = {Groups.Crear.class, Groups.Actualizar.class}, message = "El metodoPago no puede estar vacio")
    private MetodoPago metodoPago;

    @NotNull(groups = {Groups.Actualizar.class}, message = "El estadoTransaccion no puede estar vacio")
    private EstadoTransaccion estadoTransaccion;

    @NotNull(groups = {Groups.Crear.class, Groups.Actualizar.class}, message = "El vehiculo_id no puede estar vacio")
    private Long vehiculo_id;

    @NotNull(groups = {Groups.Crear.class, Groups.Actualizar.class}, message = "El cliente_id no puede estar vacio")
    private Long cliente_id;

    @NotNull(groups = {Groups.Crear.class, Groups.Actualizar.class}, message = "El vendedor_id no puede estar vacio")
    private Long vendedor_id;
}
