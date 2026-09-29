package utn.programacion3.agencia_de_autos.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import utn.programacion3.agencia_de_autos.dto.response.FavoritoResponseDTO;
import utn.programacion3.agencia_de_autos.model.Favorito;

@Mapper(componentModel = "spring", uses = VehiculoMapper.class)
public interface FavoritoMapper {

    @Mapping(target = "vehiculo.modeloNombre", source = "vehiculo.modelo.nombre")
    FavoritoResponseDTO toResponse(Favorito favorito);

}