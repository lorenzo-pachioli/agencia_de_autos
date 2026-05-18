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

    @NotNull(groups = {Groups.Crear.class, Groups.Actualizar.class}, message = "El campo observaciones puede estar vacio pero es requerido")
    private String observaciones;

    @NotNull(groups = {Groups.Crear.class, Groups.Actualizar.class}, message = "La comision calculada no puede estar vacio")
    @DecimalMin(value = "0.01", message = "La comision calculada debe ser mayor a 0")
    private BigDecimal comision_calculada;

    @NotNull(groups = {Groups.Crear.class, Groups.Actualizar.class}, message = "El precio final no puede estar vacio")
    @DecimalMin(value = "0.01", message = "El precio final debe ser mayor a 0")
    private BigDecimal precio_final;

    @NotBlank(groups = {Groups.Crear.class, Groups.Actualizar.class}, message = "El nombre no puede estar vacio")
    private MetodoPago metodoPago;

    @NotBlank(groups = {Groups.Actualizar.class}, message = "El estadoTransaccion no puede estar vacio")
    private EstadoTransaccion estadoTransaccion;

    @NotBlank(groups = {Groups.Crear.class, Groups.Actualizar.class}, message = "El vehiculo_id no puede estar vacio")
    private Long vehiculo_id;

    @NotBlank(groups = {Groups.Crear.class, Groups.Actualizar.class}, message = "El cliente_id no puede estar vacio")
    private Long cliente_id;

    @NotBlank(groups = {Groups.Crear.class, Groups.Actualizar.class}, message = "El vendedor_id no puede estar vacio")
    private Long vendedor_id;
}
