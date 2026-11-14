package utn.programacion3.agencia_de_autos.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import utn.programacion3.agencia_de_autos.dto.request.SolicitudTransaccionRequestDTO;
import utn.programacion3.agencia_de_autos.model.Transaccion;

@Mapper(componentModel = "spring")
public interface SolicitudTransaccionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created_at", ignore = true)
    @Mapping(target = "updated_at", ignore = true)
    @Mapping(target = "precio_final", ignore = true)
    @Mapping(target = "comision_calculada", ignore = true)
    @Mapping(target = "vehiculo", ignore = true)
    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "vendedor", ignore = true)
    @Mapping(target = "fecha_visita", ignore = true)
    @Mapping(target = "estadoTransaccion", constant = "SOLICITAR")
    Transaccion toEntity(SolicitudTransaccionRequestDTO dto);
}
