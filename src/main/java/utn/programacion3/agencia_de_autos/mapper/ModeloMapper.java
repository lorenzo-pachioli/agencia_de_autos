package utn.programacion3.agencia_de_autos.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import utn.programacion3.agencia_de_autos.dto.request.ModeloRequestDTO;
import utn.programacion3.agencia_de_autos.dto.response.ModeloResponseDTO;
import utn.programacion3.agencia_de_autos.model.Modelo;
import utn.programacion3.agencia_de_autos.model.Marca;

@Mapper(componentModel = "spring")
public interface ModeloMapper {

        @Mapping(target = "marca", source = "marca.nombre")
        ModeloResponseDTO toResponse(Modelo modelo);

        @Mapping(target = "marca", ignore = true)
        Modelo toEntity(ModeloRequestDTO dto);
    }