package utn.programacion3.agencia_de_autos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import utn.programacion3.agencia_de_autos.dto.request.CambiarEstadoVehiculoDTO;
import utn.programacion3.agencia_de_autos.dto.request.VehiculoFilterDTO;
import utn.programacion3.agencia_de_autos.dto.request.VehiculoRequestDTO;
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
import java.util.List;


@Service
@RequiredArgsConstructor
public class VehiculoServiceImpl implements VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final ModeloRepository modeloRepository;
    private final VehiculoMapper vehiculoMapper;

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
    public List<VehiculoResponseDTO> obtenerVehiculos() {

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
        vehiculo.setPrecioVenta(request.getPrecio());
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
    public VehiculoResponseDTO modificarPrecio(Long id, BigDecimal nuevoPrecio) {

        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(VehiculoNoEncontradoException::new);

        vehiculo.setPrecioVenta(nuevoPrecio);

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
    public List<VehiculoResponseDTO> buscarConFiltros(VehiculoFilterDTO filtros) {

        return vehiculoRepository.findAll(
                        VehiculoSpecification.conFiltros(filtros)
                )
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