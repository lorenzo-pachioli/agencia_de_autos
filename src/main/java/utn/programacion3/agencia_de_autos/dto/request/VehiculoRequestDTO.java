package utn.programacion3.agencia_de_autos.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import utn.programacion3.agencia_de_autos.model.enums.EstadoVehiculo;
import utn.programacion3.agencia_de_autos.model.enums.TipoCombustible;
import utn.programacion3.agencia_de_autos.model.enums.TipoTransmision;

import java.math.BigDecimal;

@Getter
@Setter
public class VehiculoRequestDTO {

    @NotBlank(message = "La patente es obligatoria")
    private String patente;

    @NotNull(message = "El año es obligatorio")
    private Integer anio;

    @NotNull(message = "El precio es obligatorio")
    private BigDecimal precio;

    @NotNull(message = "El modelo es obligatorio")
    private Long modeloId;

    @NotNull
    private Integer kilometraje;

    @NotBlank
    private String color;

    private String descripcion;

    @NotNull
    private EstadoVehiculo estado;

    @NotNull
    private TipoTransmision tipoTransmision;

    @NotNull
    private TipoCombustible tipoCombustible;





}