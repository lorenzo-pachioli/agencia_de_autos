package utn.programacion3.agencia_de_autos.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import utn.programacion3.agencia_de_autos.dto.request.VehiculoRequestDTO;
import utn.programacion3.agencia_de_autos.dto.response.VehiculoResponseDTO;
import utn.programacion3.agencia_de_autos.model.Vehiculo;
import utn.programacion3.agencia_de_autos.model.Modelo;

@Mapper(componentModel = "spring")
public interface VehiculoMapper {

    @Mapping(target = "modelo", source = "modelo.nombre")
    VehiculoResponseDTO toResponse(Vehiculo vehiculo);

    @Mapping(target = "modelo", source = "modeloId")
    Vehiculo toEntity(VehiculoRequestDTO dto);

    // Convierte Long -> Modelo (solo con id)
    default Modelo map(Long modeloId) {
        if (modeloId == null) {
            return null;
        }

        Modelo modelo = new Modelo();
        modelo.setId(modeloId);
        return modelo;
    }
}