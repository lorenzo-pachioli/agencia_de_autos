package utn.programacion3.agencia_de_autos.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoritoRequestDTO {
    private Long usuarioId;
    private Long vehiculoId;
}
