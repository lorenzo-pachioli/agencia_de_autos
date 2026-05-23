package utn.programacion3.agencia_de_autos.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import utn.programacion3.agencia_de_autos.dto.request.VehiculoRequestDTO;
import utn.programacion3.agencia_de_autos.dto.response.VehiculoResponseDTO;
import utn.programacion3.agencia_de_autos.model.Vehiculo;


@Mapper(componentModel = "spring")
public interface VehiculoMapper {

    @Mapping(target = "modeloNombre", source = "modelo.nombre")
    VehiculoResponseDTO toResponse(Vehiculo vehiculo);

    @Mapping(target = "modelo", ignore = true)
    Vehiculo toEntity(VehiculoRequestDTO dto);
}