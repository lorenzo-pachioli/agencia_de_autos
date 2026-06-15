package utn.programacion3.agencia_de_autos.mapper;

import org.mapstruct.Mapper;
import utn.programacion3.agencia_de_autos.dto.response.FavoritoResponseDTO;
import utn.programacion3.agencia_de_autos.model.Favorito;

@Mapper(componentModel = "spring")
public interface FavoritoMapper {

    FavoritoResponseDTO toResponse(Favorito favorito);

}