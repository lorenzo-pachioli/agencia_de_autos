package utn.programacion3.agencia_de_autos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import utn.programacion3.agencia_de_autos.dto.request.VehiculoRequestDTO;
import utn.programacion3.agencia_de_autos.dto.response.VehiculoResponseDTO;
import utn.programacion3.agencia_de_autos.mapper.VehiculoMapper;
import utn.programacion3.agencia_de_autos.model.Modelo;
import utn.programacion3.agencia_de_autos.model.Vehiculo;
import utn.programacion3.agencia_de_autos.repository.ModeloRepository;
import utn.programacion3.agencia_de_autos.repository.VehiculoRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final ModeloRepository modeloRepository;
    private final VehiculoMapper vehiculoMapper;

    // Crear vehiculo
    public VehiculoResponseDTO crearVehiculo(VehiculoRequestDTO request){

        if (vehiculoRepository.existsByPatente(request.getPatente())){
            throw new RuntimeException("La patente ya existe");
        }

        Modelo modelo = modeloRepository.findById(request.getModeloId())
                .orElseThrow(() -> new RuntimeException("Modelo no encontrado"));

        Vehiculo vehiculo = vehiculoMapper.toEntity(request);

        vehiculo.setModelo(modelo);

        Vehiculo vehiculoGuardado = vehiculoRepository.save(vehiculo);

        return vehiculoMapper.toResponse(vehiculoGuardado);
    }

    // Listar vehiculos
    public List<VehiculoResponseDTO> obtenerVehiculos(){

        return vehiculoRepository.findAll()
                .stream()
                .map(vehiculoMapper::toResponse)
                .toList();
    }

    // Obtener vehiculo por id
    public VehiculoResponseDTO obtenerVehiculoPorId(Long id){

        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehiculo no encontrado"));

        return vehiculoMapper.toResponse(vehiculo);
    }

    // Buscar por patente
    public VehiculoResponseDTO obtenerVehiculoPorPatente(String patente){

        Vehiculo vehiculo = vehiculoRepository.findByPatente(patente)
                .orElseThrow(() -> new RuntimeException("Vehiculo no encontrado"));

        return vehiculoMapper.toResponse(vehiculo);
    }

    // Actualizar vehiculo
    public VehiculoResponseDTO actualizarVehiculo(Long id, VehiculoRequestDTO request){

        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehiculo no encontrado"));

        Modelo modelo = modeloRepository.findById(request.getModeloId())
                .orElseThrow(() -> new RuntimeException("Modelo no encontrado"));

        vehiculo.setPatente(request.getPatente());
        vehiculo.setAnio(request.getAnio());
        vehiculo.setPrecio(request.getPrecio());
        vehiculo.setModelo(modelo);

        Vehiculo vehiculoActualizado = vehiculoRepository.save(vehiculo);

        return vehiculoMapper.toResponse(vehiculoActualizado);
    }

    // Modificar precio
    public VehiculoResponseDTO modificarPrecio(Long id, BigDecimal nuevoPrecio){

        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehiculo no encontrado"));

        vehiculo.setPrecio(nuevoPrecio);

        Vehiculo vehiculoActualizado = vehiculoRepository.save(vehiculo);

        return vehiculoMapper.toResponse(vehiculoActualizado);
    }

    // Eliminar vehiculo
    public void eliminarVehiculo(Long id){

        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehiculo no encontrado"));

        vehiculoRepository.delete(vehiculo);
    }
}