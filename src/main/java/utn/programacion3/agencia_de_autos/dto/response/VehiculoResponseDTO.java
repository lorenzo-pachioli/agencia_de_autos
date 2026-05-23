package utn.programacion3.agencia_de_autos.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class VehiculoResponseDTO {

    private Long id;
     private String patente;
     private String color;
     private BigDecimal precio;
     private String modeloNombre;
     private Integer anio;
}