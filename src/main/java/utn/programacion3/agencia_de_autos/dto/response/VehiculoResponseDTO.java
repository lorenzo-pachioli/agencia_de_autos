package utn.programacion3.agencia_de_autos.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
 @JsonPropertyOrder({
         "id",
         "patente",
         "modeloNombre",
         "anio",
         "precio",
         "kilometraje",
         "color",
         "estado"
 })
 public class VehiculoResponseDTO {


  private Long id;

  private String patente;

  private String modeloNombre;

  private Integer anio;

  private BigDecimal precio;

  private Integer kilometraje;

  private String color;

  private EstadoVehiculo estado;

  private TipoCombustible tipoCombustible;

  private TipoTransmision tipoTransmision;

 }