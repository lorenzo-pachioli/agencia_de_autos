package utn.programacion3.agencia_de_autos.mapper;

import org.mapstruct.Mapping;
import utn.programacion3.agencia_de_autos.dto.request.TransaccionRequestDTO;
import utn.programacion3.agencia_de_autos.dto.response.TransaccionResponseDTO;
import utn.programacion3.agencia_de_autos.model.Transaccion;

public interface TransaccionMapper {

    @Mapping(target = "id", ignore = true)
    Transaccion toEntity(TransaccionRequestDTO transaccionRequestDto);

    @Mapping(target = "vehiculo_id", source = "vehiculo.id")
    @Mapping(target = "cliente_id", source = "cliente.id")
    @Mapping(target = "vendedor_id", source = "vendedor.id")
    TransaccionResponseDTO toDto(Transaccion transaccion);
}
