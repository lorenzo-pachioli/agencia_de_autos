package utn.programacion3.agencia_de_autos.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import utn.programacion3.agencia_de_autos.dto.request.TransaccionRequestDTO;
import utn.programacion3.agencia_de_autos.dto.response.TransaccionResponseDTO;
import utn.programacion3.agencia_de_autos.model.Transaccion;
import utn.programacion3.agencia_de_autos.model.enums.EstadoTransaccion;

@Mapper(componentModel = "spring")
public interface TransaccionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created_at", ignore = true)
    @Mapping(target = "updated_at", ignore = true)
    @Mapping(target = "comision_calculada", ignore = true)
    @Mapping(target = "vehiculo", ignore = true)
    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "vendedor", ignore = true)
    @Mapping(target = "estadoTransaccion", constant = "RESERVA")
    Transaccion toEntity(TransaccionRequestDTO dto);

    @Mapping(target = "vehiculo_id", source = "vehiculo.id")
    @Mapping(target = "cliente_id", source = "cliente.id")
    @Mapping(target = "vendedor_id", source = "vendedor.id")
    @Mapping(target = "patente", source = "vehiculo.patente")
    @Mapping(target = "cliente_email", source = "cliente.email")
    @Mapping(target = "vendedor_email", source = "vendedor.email")
    TransaccionResponseDTO toResponseDTO(Transaccion transaccion);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created_at", ignore = true)
    @Mapping(target = "updated_at", ignore = true)
    @Mapping(target = "comision_calculada", ignore = true)
    @Mapping(target = "vehiculo", ignore = true)
    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "vendedor", ignore = true)
    void updateEntityFromDto(TransaccionRequestDTO dto, @MappingTarget Transaccion transaccion);
}