package utn.programacion3.agencia_de_autos.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import utn.programacion3.agencia_de_autos.dto.request.UsuarioAdminRequestDto;
import utn.programacion3.agencia_de_autos.dto.request.UsuarioRequestDTO;
import utn.programacion3.agencia_de_autos.dto.response.UsuarioResponseDTO;
import utn.programacion3.agencia_de_autos.model.Usuario;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioResponseDTO toResponseDTO(Usuario usuario);

    // De Usuario comun (sin rol) a Entidad
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rolUsuario", ignore = true)
    @Mapping(target = "activo", ignore = true)
    @Mapping(target = "alta", ignore = true)
    @Mapping(target = "favoritos", ignore = true)
    Usuario toEntity(UsuarioRequestDTO dto);

    // De AdminRequestDTO (con rol) a Entidad
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", ignore = true)
    @Mapping(target = "alta", ignore = true)
    @Mapping(target = "favoritos", ignore = true)
    Usuario toEntity(UsuarioAdminRequestDto dto);
}
