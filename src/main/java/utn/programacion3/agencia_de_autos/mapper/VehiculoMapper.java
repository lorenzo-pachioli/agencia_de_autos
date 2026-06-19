package utn.programacion3.agencia_de_autos.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import utn.programacion3.agencia_de_autos.dto.request.ReporteGananciaVehiculoDTO;
import utn.programacion3.agencia_de_autos.dto.request.VehiculoRequestDTO;
import utn.programacion3.agencia_de_autos.dto.response.VehiculoPublicResponseDTO;
import utn.programacion3.agencia_de_autos.dto.response.VehiculoResponseDTO;
import utn.programacion3.agencia_de_autos.model.Vehiculo;


@Mapper(componentModel = "spring")
public interface VehiculoMapper {

    @Mapping(target = "modeloNombre", source = "modelo.nombre")
    @Mapping(target = "imagenPrincipalUrl", ignore = true)
    @Mapping(target = "imagenes", ignore = true)
    VehiculoResponseDTO toResponse(Vehiculo vehiculo);

    @Mapping(target = "modeloNombre", source = "modelo.nombre")
    VehiculoPublicResponseDTO toPublicResponse(Vehiculo vehiculo);

    @Mapping(target = "modelo", source = "modelo.nombre")
    @Mapping(
            target = "ganancia",
            expression = "java(vehiculo.getPrecioVenta().subtract(vehiculo.getPrecioAdquisicion()))"
    )
    ReporteGananciaVehiculoDTO toReporteGanancia(Vehiculo vehiculo);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "modelo", ignore = true)
    @Mapping(target = "imagenes", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Vehiculo toEntity(VehiculoRequestDTO dto);
}