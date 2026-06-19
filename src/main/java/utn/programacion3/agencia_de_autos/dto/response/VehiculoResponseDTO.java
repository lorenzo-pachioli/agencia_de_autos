package utn.programacion3.agencia_de_autos.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import utn.programacion3.agencia_de_autos.model.enums.EstadoVehiculo;
import utn.programacion3.agencia_de_autos.model.enums.TipoCombustible;
import utn.programacion3.agencia_de_autos.model.enums.TipoTransmision;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@JsonPropertyOrder({
        "id",
        "patente",
        "modeloNombre",
        "anio",
        "precioVenta",
        "kilometraje",
        "color",
        "descripcion",
        "estado",
        "tipoCombustible",
        "tipoTransmision",
        "imagenPrincipalUrl",
        "imagenes",
        "createdAt",
        "updatedAt"
})
public class VehiculoResponseDTO {

 private Long id;

 private String patente;

 private String modeloNombre;

 private Integer anio;

 private BigDecimal precioVenta;

 private Integer kilometraje;

 private String color;

 private String descripcion;

 private EstadoVehiculo estado;

 private TipoCombustible tipoCombustible;

 private TipoTransmision tipoTransmision;

 private String imagenPrincipalUrl;

 @Builder.Default
 private List<String> imagenes = new ArrayList<>();

 private LocalDateTime createdAt;

 private LocalDateTime updatedAt;
}