package utn.programacion3.agencia_de_autos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoritoResponseDTO {

    private Long id;
    private VehiculoPublicResponseDTO vehiculo;

}
