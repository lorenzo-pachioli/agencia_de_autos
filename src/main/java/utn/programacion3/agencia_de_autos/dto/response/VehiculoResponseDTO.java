package utn.programacion3.agencia_de_autos.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class VehiculoResponseDTO {

    private Long id;
    private String modelo;
}