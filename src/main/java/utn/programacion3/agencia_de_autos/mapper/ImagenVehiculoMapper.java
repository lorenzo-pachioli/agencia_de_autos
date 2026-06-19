package utn.programacion3.agencia_de_autos.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import utn.programacion3.agencia_de_autos.dto.request.ImagenVehiculoRequestDTO;
import utn.programacion3.agencia_de_autos.dto.response.ImagenVehiculoResponseDTO;
import utn.programacion3.agencia_de_autos.model.ImagenVehiculo;


@Mapper(componentModel = "spring")
public interface ImagenVehiculoMapper {

    @Mapping(source = "vehiculo.patente", target = "patenteVehiculo")
    ImagenVehiculoResponseDTO toResponse(ImagenVehiculo entity);

    @Mapping(target = "vehiculo", ignore = true)
    @Mapping(target = "esPrincipal", source = "esPrincipal", defaultValue = "false")
    ImagenVehiculo toEntity(ImagenVehiculoRequestDTO dto);
}