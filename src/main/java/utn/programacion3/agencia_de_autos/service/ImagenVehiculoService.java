package utn.programacion3.agencia_de_autos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import utn.programacion3.agencia_de_autos.dto.request.ImagenVehiculoRequestDTO;
import utn.programacion3.agencia_de_autos.dto.response.ImagenVehiculoResponseDTO;
import utn.programacion3.agencia_de_autos.mapper.ImagenVehiculoMapper;
import utn.programacion3.agencia_de_autos.model.ImagenVehiculo;
import utn.programacion3.agencia_de_autos.model.Vehiculo;
import utn.programacion3.agencia_de_autos.repository.ImagenVehiculoRepository;
import utn.programacion3.agencia_de_autos.repository.VehiculoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImagenVehiculoService {

    private final ImagenVehiculoRepository imagenVehiculoRepository;
    private final VehiculoRepository vehiculoRepository;
    private final ImagenVehiculoMapper imagenVehiculoMapper;

    // Crear imagen
    public ImagenVehiculoResponseDTO crearImagen(ImagenVehiculoRequestDTO request){

        Vehiculo vehiculo = vehiculoRepository.findById(request.getVehiculoId())
                .orElseThrow(() -> new RuntimeException("Vehiculo no encontrado"));

        ImagenVehiculo imagen = imagenVehiculoMapper.toEntity(request);

        imagen.setVehiculo(vehiculo);

        ImagenVehiculo imagenGuardada = imagenVehiculoRepository.save(imagen);

        return imagenVehiculoMapper.toResponse(imagenGuardada);
    }

    // Obtener todas las imagenes
    public List<ImagenVehiculoResponseDTO> obtenerTodas(){

        return imagenVehiculoRepository.findAll()
                .stream()
                .map(imagenVehiculoMapper::toResponse)
                .toList();
    }

    // Obtener imagen por ID
    public ImagenVehiculoResponseDTO obtenerPorId(Long id){

        ImagenVehiculo imagen = imagenVehiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));

        return imagenVehiculoMapper.toResponse(imagen);
    }

    // Obtener imagenes por vehiculo
    public List<ImagenVehiculoResponseDTO> obtenerPorVehiculo(Long vehiculoId){

        return imagenVehiculoRepository.findByVehiculoId(vehiculoId)
                .stream()
                .map(imagenVehiculoMapper::toResponse)
                .toList();
    }

    // Eliminar imagen
    public void eliminarImagen(Long id){

        ImagenVehiculo imagen = imagenVehiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));

        imagenVehiculoRepository.delete(imagen);
    }
}
