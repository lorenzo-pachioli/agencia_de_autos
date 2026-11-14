package utn.programacion3.agencia_de_autos.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import utn.programacion3.agencia_de_autos.model.enums.EstadoVehiculo;
import utn.programacion3.agencia_de_autos.model.enums.TipoCombustible;
import utn.programacion3.agencia_de_autos.model.enums.TipoTransmision;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class VehiculoGestionResponseDTO {

    private Long id;

    private String patente;

    private Long marcaId;

    private String marcaNombre;

    private Long modeloId;

    private String modeloNombre;

    private Integer anio;

    private BigDecimal precioAdquisicion;

    private BigDecimal precioVenta;

    private Integer kilometraje;

    private String color;

    private String descripcion;

    private EstadoVehiculo estado;

    private TipoCombustible tipoCombustible;

    private TipoTransmision tipoTransmision;
}
