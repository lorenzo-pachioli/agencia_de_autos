package utn.programacion3.agencia_de_autos.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import utn.programacion3.agencia_de_autos.dto.request.ImagenVehiculoRequestDTO;
import utn.programacion3.agencia_de_autos.dto.response.ImagenVehiculoResponseDTO;
import utn.programacion3.agencia_de_autos.model.ImagenVehiculo;
import utn.programacion3.agencia_de_autos.model.Vehiculo;

@Mapper(componentModel = "spring")
public interface ImagenVehiculoMapper {

    ImagenVehiculoResponseDTO toResponse(ImagenVehiculo entity);

    @Mapping(target = "vehiculo", source = "vehiculoId")
    ImagenVehiculo toEntity(ImagenVehiculoRequestDTO dto);

    // Convierte Long -> Vehiculo (solo con id)
    default Vehiculo map(Long vehiculoId) {
        if (vehiculoId == null) return null;

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setId(vehiculoId);
        return vehiculo;
    }
}