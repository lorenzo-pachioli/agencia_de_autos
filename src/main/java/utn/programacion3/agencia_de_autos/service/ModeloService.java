package utn.programacion3.agencia_de_autos.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import utn.programacion3.agencia_de_autos.dto.request.ModeloRequestDTO;
import utn.programacion3.agencia_de_autos.dto.response.ModeloResponseDTO;
import utn.programacion3.agencia_de_autos.mapper.ModeloMapper;
import utn.programacion3.agencia_de_autos.model.Marca;
import utn.programacion3.agencia_de_autos.model.Modelo;
import utn.programacion3.agencia_de_autos.repository.MarcaRepository;
import utn.programacion3.agencia_de_autos.repository.ModeloRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModeloService {

    private final ModeloRepository modeloRepository;
    private final MarcaRepository marcaRepository;
    private final ModeloMapper modeloMapper;

    // Crear un nuevo modelo
    public ModeloResponseDTO crearModelo(ModeloRequestDTO request) {

        Marca marca = marcaRepository.findById(request.getMarcaId())
                .orElseThrow(() -> new RuntimeException("Marca no encontrada"));

        Modelo modelo = modeloMapper.toEntity(request);

        modelo.setMarca(marca);
        modelo.setAnio(request.getAnio());
        modelo.setVersion(request.getVersion());

        Modelo modeloGuardado = modeloRepository.save(modelo);

        return modeloMapper.toResponse(modeloGuardado);
    }

    // Obtener todos los modelos
    public List<ModeloResponseDTO> listarModelos() {

        return modeloRepository.findAll()
                .stream()
                .map(modeloMapper::toResponse)
                .toList();
    }

    // Obtener modelo por ID
    public ModeloResponseDTO obtenerModeloPorId(Long id) {

        Modelo modelo = modeloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Modelo no encontrado"));

        return modeloMapper.toResponse(modelo);
    }

    // Actualizar modelo
    public ModeloResponseDTO actualizarModelo(Long id, ModeloRequestDTO request) {

        Modelo modelo = modeloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Modelo no encontrado"));

        Marca marca = marcaRepository.findById(request.getMarcaId())
                .orElseThrow(() -> new RuntimeException("Marca no encontrada"));

        modelo.setNombre(request.getNombre());
        modelo.setMarca(marca);

        Modelo modeloActualizado = modeloRepository.save(modelo);

        return modeloMapper.toResponse(modeloActualizado);
    }

    // Eliminar modelo
    public void eliminarModelo(Long id) {

        Modelo modelo = modeloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Modelo no encontrado"));

        modeloRepository.delete(modelo);
    }
}