package utn.programacion3.agencia_de_autos.mapper;


import org.mapstruct.Mapper;
import utn.programacion3.agencia_de_autos.dto.response.AuditoriaTransaccionResponseDTO;
import utn.programacion3.agencia_de_autos.model.AuditoriaTransaccion;

@Mapper(componentModel = "spring")
public interface AuditoriaTransaccionMapper {

    AuditoriaTransaccionResponseDTO toResponseDTO(AuditoriaTransaccion transaccion);
}
