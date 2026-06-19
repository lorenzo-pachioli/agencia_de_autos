package utn.programacion3.agencia_de_autos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import utn.programacion3.agencia_de_autos.dto.request.*;
import utn.programacion3.agencia_de_autos.dto.response.ReporteGananciasResponseDTO;
import utn.programacion3.agencia_de_autos.dto.response.VehiculoPublicResponseDTO;
import utn.programacion3.agencia_de_autos.dto.response.VehiculoResponseDTO;
import utn.programacion3.agencia_de_autos.exception.ModeloNoEncontradoException;
import utn.programacion3.agencia_de_autos.exception.PatenteDuplicadaException;
import utn.programacion3.agencia_de_autos.exception.VehiculoNoEncontradoException;
import utn.programacion3.agencia_de_autos.mapper.VehiculoMapper;
import utn.programacion3.agencia_de_autos.model.Modelo;
import utn.programacion3.agencia_de_autos.model.Vehiculo;
import utn.programacion3.agencia_de_autos.model.enums.EstadoVehiculo;
import utn.programacion3.agencia_de_autos.repository.ModeloRepository;
import utn.programacion3.agencia_de_autos.repository.VehiculoRepository;
import utn.programacion3.agencia_de_autos.repository.VehiculoSpecification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class VehiculoServiceImpl implements VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final ModeloRepository modeloRepository;
    private final VehiculoMapper vehiculoMapper;
    private final ImagenVehiculoService imagenVehiculoService;

    @Override
    public VehiculoResponseDTO crearVehiculo(VehiculoRequestDTO request) {

        if (vehiculoRepository.existsByPatente(request.getPatente())) {
            throw new PatenteDuplicadaException();
        }

        Modelo modelo = modeloRepository.findById(request.getModeloId())
                .orElseThrow(ModeloNoEncontradoException::new);

        Vehiculo vehiculo = vehiculoMapper.toEntity(request);

        vehiculo.setModelo(modelo);

        Vehiculo vehiculoGuardado = vehiculoRepository.save(vehiculo);

        return vehiculoMapper.toResponse(vehiculoGuardado);
    }

    @Override
    public Vehiculo obtenerVehiculoEntityPorId(Long id) {

        return vehiculoRepository.findById(id)
                .orElseThrow(VehiculoNoEncontradoException::new);
    }
    @Override
    public VehiculoResponseDTO obtenerVehiculoPorId(Long id){
        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(VehiculoNoEncontradoException::new);
        VehiculoResponseDTO dto = vehiculoMapper.toResponse(vehiculo);
        dto.setImagenes(imagenVehiculoService.obtenerTodasLasUrls(id));
        dto.setImagenPrincipalUrl(
                imagenVehiculoService.obtenerUrlPrincipal(vehiculo.getId())
        );
        return dto;
    }

    @Override
    public List<VehiculoPublicResponseDTO> obtenerVehiculosPublicos() {

        return vehiculoRepository.findAll()
                .stream()
                .map(vehiculoMapper::toPublicResponse)
                .toList();
    }

    @Override
    public List<VehiculoResponseDTO> obtenerVehiculosInternos() {

        return vehiculoRepository.findAll()
                .stream()
                .map(vehiculoMapper::toResponse)
                .toList();
    }

    @Override
    public VehiculoResponseDTO actualizarVehiculo(Long id, VehiculoRequestDTO request) {

        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(VehiculoNoEncontradoException::new);

        Modelo modelo = modeloRepository.findById(request.getModeloId())
                .orElseThrow(ModeloNoEncontradoException::new);

        vehiculo.setPatente(request.getPatente());
        vehiculo.setAnio(request.getAnio());
        vehiculo.setPrecioVenta(request.getPrecioVenta());
        vehiculo.setKilometraje(request.getKilometraje());
        vehiculo.setColor(request.getColor());
        vehiculo.setEstado(request.getEstado());
        vehiculo.setModelo(modelo);
        vehiculo.setDescripcion(request.getDescripcion());
        vehiculo.setTipoCombustible(request.getTipoCombustible());
        vehiculo.setTipoTransmision(request.getTipoTransmision());

        Vehiculo vehiculoActualizado = vehiculoRepository.save(vehiculo);

        return vehiculoMapper.toResponse(vehiculoActualizado);
    }

    @Override
    public VehiculoResponseDTO cambiarEstado(Long id, CambiarEstadoVehiculoDTO request) {

        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(VehiculoNoEncontradoException::new);

        vehiculo.setEstado(request.getEstado());

        Vehiculo vehiculoGuardado = vehiculoRepository.save(vehiculo);

        return vehiculoMapper.toResponse(vehiculoGuardado);
    }

    @Override
    public Vehiculo cambiarEstadoEntity(Long id, EstadoVehiculo estado) {

        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(VehiculoNoEncontradoException::new);

        vehiculo.setEstado(estado);

        return vehiculoRepository.save(vehiculo);
    }

    @Override
    public Page<VehiculoResponseDTO> buscarConFiltros(VehiculoFilterDTO filtros, Pageable pageable) {

        return vehiculoRepository.findAll(VehiculoSpecification.conFiltros(filtros), pageable)
                .map(vehiculo -> {
                    VehiculoResponseDTO dto = vehiculoMapper.toResponse(vehiculo);
                    dto.setImagenPrincipalUrl(
                            imagenVehiculoService.obtenerUrlPrincipal(vehiculo.getId())
                    );
                    return dto;
                });
    }

    @Override
    public ReporteGananciasResponseDTO obtenerReporteGanancias() {

        List<Vehiculo> vehiculos = vehiculoRepository.findAll()
                .stream()
                .filter(v -> v.getEstado() == EstadoVehiculo.VENDIDO)
                .toList();

        List<ReporteGananciaVehiculoDTO> detalle = vehiculos.stream()
                .map(vehiculoMapper::toReporteGanancia)
                .toList();

        BigDecimal totalInvertido = vehiculos.stream()
                .map(Vehiculo::getPrecioAdquisicion)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalVenta = vehiculos.stream()
                .map(Vehiculo::getPrecioVenta)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal gananciaTotal = totalVenta.subtract(totalInvertido);

        return ReporteGananciasResponseDTO.builder()
                .totalInvertido(totalInvertido)
                .totalVenta(totalVenta)
                .gananciaTotal(gananciaTotal)
                .vehiculos(detalle)
                .build();
    }

    @Override
    public ReporteStockVehiculoDTO obtenerReporteStock() {

        long disponibles = vehiculoRepository.countByEstado(EstadoVehiculo.DISPONIBLE);

        long reservados = vehiculoRepository.countByEstado(EstadoVehiculo.RESERVADO);

        long vendidos = vehiculoRepository.countByEstado(EstadoVehiculo.VENDIDO);

        long baja = vehiculoRepository.countByEstado(EstadoVehiculo.BAJA);

        return ReporteStockVehiculoDTO.builder()
                .disponibles(disponibles)
                .reservados(reservados)
                .vendidos(vendidos)
                .baja(baja)
                .total(disponibles + reservados + vendidos + baja)
                .build();
    }

    @Override
    public List<VehiculoResponseDTO> obtenerUltimosVehiculos(Integer cantidad) {

        Pageable pageable = PageRequest.of(
                0,
                cantidad,
                Sort.by("createdAt").descending()
        );

        return vehiculoRepository.findAll(pageable)
                .stream()
                .map(vehiculoMapper::toResponse)
                .toList();
    }



    @Override
    public void eliminarVehiculo(Long id) {

        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(VehiculoNoEncontradoException::new);

        vehiculoRepository.delete(vehiculo);
    }


}