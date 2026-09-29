package utn.programacion3.agencia_de_autos.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImagenVehiculoResponseDTO {

    private Long id;
    private String url;
    private Boolean esPrincipal;
    private String patenteVehiculo;
}