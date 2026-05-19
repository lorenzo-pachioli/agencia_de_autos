package utn.programacion3.agencia_de_autos.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ImagenVehiculoResponseDTO {

    private Long id;
    private String url;
}