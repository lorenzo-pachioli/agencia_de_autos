package utn.programacion3.agencia_de_autos.service;

import org.springframework.stereotype.Service;
import utn.programacion3.agencia_de_autos.dto.request.CambiarEstadoVehiculoDTO;
import utn.programacion3.agencia_de_autos.dto.request.ReporteGananciaVehiculoDTO;
import utn.programacion3.agencia_de_autos.dto.request.VehiculoFilterDTO;
import utn.programacion3.agencia_de_autos.dto.request.VehiculoRequestDTO;
import utn.programacion3.agencia_de_autos.dto.response.ReporteGananciasResponseDTO;
import utn.programacion3.agencia_de_autos.dto.response.VehiculoPublicResponseDTO;
import utn.programacion3.agencia_de_autos.dto.response.VehiculoResponseDTO;
import utn.programacion3.agencia_de_autos.model.Vehiculo;
import utn.programacion3.agencia_de_autos.model.enums.EstadoVehiculo;

import java.math.BigDecimal;
import java.util.List;

@Service

public interface VehiculoService {

    VehiculoResponseDTO crearVehiculo(VehiculoRequestDTO request);

    Vehiculo obtenerVehiculoEntityPorId(Long id);


    VehiculoResponseDTO actualizarVehiculo(Long id, VehiculoRequestDTO request);

    VehiculoResponseDTO modificarPrecio(Long id, BigDecimal nuevoPrecio);

    VehiculoResponseDTO cambiarEstado(Long id, CambiarEstadoVehiculoDTO request);

    Vehiculo cambiarEstadoEntity(Long id, EstadoVehiculo estado);

    List<VehiculoResponseDTO> buscarConFiltros(VehiculoFilterDTO filtros);

    void eliminarVehiculo(Long id);

    List<VehiculoPublicResponseDTO> obtenerVehiculosPublicos();

    List<VehiculoResponseDTO> obtenerVehiculosInternos();

    ReporteGananciasResponseDTO obtenerReporteGanancias();

}