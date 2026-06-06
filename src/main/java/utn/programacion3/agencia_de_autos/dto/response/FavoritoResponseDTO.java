package utn.programacion3.agencia_de_autos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import utn.programacion3.agencia_de_autos.model.Vehiculo;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoritoResponseDTO {

    private Long id;
    private Vehiculo vehiculo;

}
