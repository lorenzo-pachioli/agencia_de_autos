package utn.programacion3.agencia_de_autos.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import utn.programacion3.agencia_de_autos.dto.request.TransaccionRequestDTO;
import utn.programacion3.agencia_de_autos.dto.response.TransaccionResponseDTO;
import utn.programacion3.agencia_de_autos.model.Transaccion;
import utn.programacion3.agencia_de_autos.model.Usuario;
import utn.programacion3.agencia_de_autos.model.Vehiculo;

@Mapper(componentModel = "spring")
public interface TransaccionMapper {

    @Mapping(target = "id", ignore = true)
    Transaccion toEntity(TransaccionRequestDTO transaccionRequestDto);

    @Mapping(target = "vehiculo_id", source = "vehiculo.id")
    @Mapping(target = "cliente_id", source = "cliente.id")
    @Mapping(target = "vendedor_id", source = "vendedor.id")
    TransaccionResponseDTO toResponseDTO(Transaccion transaccion);

    @Mapping(target = "cliente", source = "cliente")
    @Mapping(target = "vendedor", source = "vendedor")
    @Mapping(target = "vehiculo", source = "vehiculo")
    void updateEntityFromDto(
            TransaccionRequestDTO dto,
            Usuario cliente,
            Usuario vendedor,
            Vehiculo vehiculo,
            @MappingTarget Transaccion transaccion
    );
}
