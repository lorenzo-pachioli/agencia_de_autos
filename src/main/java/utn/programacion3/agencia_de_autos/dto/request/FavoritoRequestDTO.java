package utn.programacion3.agencia_de_autos.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoritoRequestDTO {

    @Schema(description = "Identificador único del usuario que marca el favorito", example = "12", minimum = "1")
    private Long usuarioId;

    @Schema(description = "Identificador único del vehículo a marcar como favorito", example = "45", minimum = "1")
    private Long vehiculoId;
}
