package utn.programacion3.agencia_de_autos.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import utn.programacion3.agencia_de_autos.model.enums.TipoCombustible;
import utn.programacion3.agencia_de_autos.model.enums.TipoTransmision;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@JsonPropertyOrder({
        "id",
        "modeloNombre",
        "anio",
        "precio",
        "kilometraje",
        "color",
        "tipoCombustible",
        "tipoTransmision"
})
public class VehiculoPublicResponseDTO {

    private Long id;

    private String modeloNombre;

    private Integer anio;

    private BigDecimal precioVenta;

    private Integer kilometraje;

    private String color;

    private TipoCombustible tipoCombustible;

    private TipoTransmision tipoTransmision;
}