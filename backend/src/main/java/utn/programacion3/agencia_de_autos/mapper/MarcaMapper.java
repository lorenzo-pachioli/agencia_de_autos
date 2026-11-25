package utn.programacion3.agencia_de_autos.mapper;

import org.mapstruct.Mapping;
import org.mapstruct.Mapper;
import utn.programacion3.agencia_de_autos.dto.request.MarcaRequestDTO;
import utn.programacion3.agencia_de_autos.dto.response.MarcaResponseDTO;
import utn.programacion3.agencia_de_autos.model.Marca;

@Mapper(componentModel = "spring")
public interface MarcaMapper {
    @Mapping(target = "eliminado", ignore = true)
    Marca toEntity(MarcaRequestDTO dto);

    MarcaResponseDTO toResponse(Marca marca);
}
